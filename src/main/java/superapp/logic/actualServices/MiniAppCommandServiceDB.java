package superapp.logic.actualServices;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.command.InvocationUser;
import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.command.TargetObject;
import superapp.converters.MiniAppCommandConverter;
import superapp.dal.MiniAppCommandCrud;
import superapp.dal.ObjectCrud;
import superapp.dal.UserCrud;
import superapp.data.MiniAppCommandEntity;
import superapp.data.MiniAppCommandPrimaryKeyId;
import superapp.data.ObjectEntity;
import superapp.data.ObjectPrimaryKeyId;
import superapp.data.UserPrimaryKeyId;
import superapp.data.UserRole;
import superapp.exceptions.DeprecatedException;
import superapp.exceptions.ForbbidenException;
import superapp.exceptions.InvalidInputException;
import superapp.exceptions.ResourceAlreadyExistException;
import superapp.exceptions.ResourceNotFoundException;
import superapp.logic.GeneralService;
import superapp.logic.MiniAppCommandAsyncService;
import superapp.logic.commands.MiniappInterface;
import superapp.utils.Validator;


@Service
public class MiniAppCommandServiceDB extends GeneralService implements MiniAppCommandAsyncService {
	private MiniAppCommandCrud commandsCrud;
	private ObjectCrud objectCrud;
	private UserCrud userCrud;
	private MiniAppCommandConverter commandConverter;
	private String superAppName;
	private JmsTemplate jmsTemplate;
	private ObjectMapper jackson;
	private final Log logger = LogFactory.getLog(MiniAppCommandServiceDB.class);
	private ApplicationContext applicationContext;

	@PostConstruct
	public void init() {
		this.jackson = new ObjectMapper();
	}

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Autowired
	public void setCommandsCrud(MiniAppCommandCrud commandsCrud) {
		this.commandsCrud = commandsCrud;
	}

	@Autowired
	public void setObjectCrud(ObjectCrud objectCrud) {
		this.objectCrud = objectCrud;
	}

	@Autowired
	public void setUserCrud(UserCrud userCrud) {
		this.userCrud = userCrud;
	}

	@Autowired
	public void setCommandConverter(MiniAppCommandConverter commandConverter) {
		this.commandConverter = commandConverter;
	}

	@Autowired
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
		this.jmsTemplate.setDeliveryDelay(10000L);
	}


	@Value("${spring.application.name:defaultValue}")
	public void setSuperAppName(String superAppName) {
		this.superAppName = superAppName;
	}

	@Override
	public Object invokeCommand(MiniAppCommandBoundary miniappCommand) {
		// Check if command boundary is valid and initialize it with id and timestamp
		logger.trace("invokeCommand method is called with command: " + miniappCommand.getCommand());
		checkAndInitCommand(miniappCommand);

		MiniAppCommandEntity newEntity = commandConverter.toEntity(miniappCommand);
		commandsCrud.save(newEntity);

		logger.debug("The command is saved in the DB with id: " + newEntity.getCommandID());

		String commandString = miniappCommand.getCommand();

		MiniappInterface command;
		Object result;
		try {
			logger.trace("The command to invoke is : " + commandString);
			command = this.applicationContext.getBean(commandString, MiniappInterface.class);
			result = command.activateCommand(miniappCommand);
		}catch (Exception e) {
			this.logger.warn("The command " + commandString + " is not found" +e.getMessage() );
			throw new InvalidInputException("command", commandString);
		}

		logger.trace("The command " + commandString + " is invoked successfully");
		return result;
	}

	// TODO: write this function (async logic)
	@Override
	public Object invokeCommandAsync(MiniAppCommandBoundary command) {
		// Check if command boundary is valid and initialize it with id and timestamp
		checkAndInitCommand(command);

		command.getCommandAttributes().put("status", "waiting...");

		try {
			String json = this.jackson.writeValueAsString(command);
			this.jmsTemplate.convertAndSend("asyncCommandsQueue", json);
			return command;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MiniAppCommandBoundary> getAllCommands(String userSuperapp, String email, int size, int page) {
		logger.trace("getAllCommands method is called");
		UserPrimaryKeyId user = new UserPrimaryKeyId(userSuperapp, email);
		if (!isValidUserCredentials(user, UserRole.ADMIN, this.userCrud)) {
			logger.warn("The user " + user.getEmail() + " is not authorized to get all commands");
			throw new ForbbidenException(user.getEmail(), "get all miniapp commands");
		}

		return this.commandsCrud.findAll(PageRequest.of(page, size, Direction.DESC, "invocationTimestamp", "commandId"))
				.stream()
				.map(this.commandConverter::toBoundary)
				.toList();
	}
	@Override
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName, String userSuperapp, String email,
															  int size, int page) {
		logger.trace("getAllMiniAppCommands method is called with miniapp name: " + miniAppName);
		UserPrimaryKeyId user = new UserPrimaryKeyId(userSuperapp, email);
		if (!isValidUserCredentials(user, UserRole.ADMIN, this.userCrud)) {
			logger.warn("The user " + user.getEmail() + " is not authorized to get all commands");
			throw new ForbbidenException(user.getEmail(), "get all miniapp commands");
		}

		return this.commandsCrud.findAllByCommandID_Miniapp(miniAppName,PageRequest.of(page, size, Direction.DESC, "invocationTimestamp", "commandId"))
				.stream()
				.map(this.commandConverter::toBoundary)
				.toList();
	}

	@Override
	public void deleteAllCommands(String superapp, String email) {
		logger.trace("deleteAllCommands method is called with superapp: " + superapp + " and email: " + email);
		UserPrimaryKeyId user = new UserPrimaryKeyId(superapp, email);
		if (!isValidUserCredentials(user, UserRole.ADMIN, this.userCrud)) {
			logger.warn("The user " + user.getEmail() + " is not authorized to delete all commands");
			throw new ForbbidenException(user.getEmail(), "delete all miniapp commands");
		}
		this.commandsCrud.deleteAll();
		logger.info("All commands are deleted");
	}


	private void checkInvokedCommand(MiniAppCommandBoundary command){
		if (command == null) {
			logger.warn("command is null");
			throw new InvalidInputException(null, "command", "invoke command");
		}

		if (command.getCommandId() == null) {
			logger.warn("command id is null");
			throw new InvalidInputException(command.getCommandId(), "command id", "invoke command");
		}

		if (command.getCommandId().getMiniapp() == null || command.getCommandId().getMiniapp().isBlank()) {
			logger.warn("command miniapp is null or empty");
			throw new InvalidInputException(command.getCommandId().getMiniapp(), "miniapp", "invoke command");
		}

		if (command.getCommandAttributes() == null) {
			logger.warn("command attributes is null");
			throw new InvalidInputException(command.getCommandAttributes(), "command attributes", "invoke command");
		}

		if (command.getCommand() == null || command.getCommand().isBlank()) {
			logger.warn("command string is null or empty");
			throw new InvalidInputException(command, "command string", "invoke command");
		}

		InvocationUser invokedBy = command.getInvokedBy();
		if (invokedBy == null || invokedBy.getUserId() == null) {
			logger.warn("invokedBy is null or userId is null");
			throw new InvalidInputException(invokedBy, "user data", "invoke command");
		}

		if (invokedBy.getUserId().getSuperapp() == null || invokedBy.getUserId().getSuperapp().isBlank()) {
			logger.warn("invokedBy superapp is null or empty");
			throw new InvalidInputException(invokedBy.getUserId().getSuperapp(), "user superapp", "invoke command");
		}

		if (invokedBy.getUserId().getEmail() == null ||
				invokedBy.getUserId().getEmail().isBlank() ||
				!Validator.isValidEmail(invokedBy.getUserId().getEmail())) {
			logger.warn("invokedBy email is null or empty or invalid");
			throw new InvalidInputException(invokedBy.getUserId().getEmail(), "user email", "invoke command");
		}

		UserPrimaryKeyId user = new UserPrimaryKeyId(invokedBy.getUserId().getSuperapp(), invokedBy.getUserId().getEmail());
		if(!isValidUserCredentials(user, UserRole.MINIAPP_USER, this.userCrud)) {
			logger.warn("user " + user + " is not authorized to invoke command");
			throw new ForbbidenException(user.getEmail(), "invoke command");
		}

		TargetObject targetObject = command.getTargetObject();
		if (targetObject == null ||
				targetObject.getObjectId() == null ||
				targetObject.getObjectId().getSuperapp() == null ||
				targetObject.getObjectId().getInternalObjectId() == null ||
				targetObject.getObjectId().getSuperapp().isBlank() ||
				targetObject.getObjectId().getInternalObjectId().isBlank()) {
			logger.warn("target object is null or invalid");
			throw new InvalidInputException(command, "target object", "invoke command");
		}

		ObjectPrimaryKeyId objId = new ObjectPrimaryKeyId(	targetObject.getObjectId().getSuperapp(),
				targetObject.getObjectId().getInternalObjectId());
		Optional<ObjectEntity> objectE = this.objectCrud.findById(objId);
		if(objectE.isEmpty() || !objectE.get().getActive()) {
			logger.warn("object not found or not active");
			throw new ResourceNotFoundException(objId, "invoke command");
		}
	}

	private void checkAndInitCommand(MiniAppCommandBoundary command) {
		// Check command validity and throw appropriate exception if not valid:
		checkInvokedCommand(command);

		command.setInvocationTimestamp(new Date());
		command.getCommandId().setSuperapp(superAppName);
		command.getCommandId().setInternalCommandId(UUID.randomUUID().toString());
		MiniAppCommandPrimaryKeyId id = new MiniAppCommandPrimaryKeyId(command.getCommandId().getSuperapp(),
				command.getCommandId().getMiniapp(), command.getCommandId().getInternalCommandId());

		logger.trace("command id set successfully to: " + id);

		Optional<MiniAppCommandEntity> entity = commandsCrud.findById(id);
		if (entity.isPresent()) {
			logger.warn("command already exists");
			throw new ResourceAlreadyExistException(id, "invoke command");
		}

		if (command.getCommandAttributes() == null) {
			command.setCommandAttributes(new HashMap<>());
		}
	}

	@Deprecated
	@Override
	public List<MiniAppCommandBoundary> getAllCommands() {
		throw new DeprecatedException(LocalDate.of(2023, 5, 22));
	}

	@Deprecated
	@Override
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {
		throw new DeprecatedException(LocalDate.of(2023, 5, 22));
	}

	@Deprecated
	@Override
	public void deleteAllCommands() {
		throw new DeprecatedException(LocalDate.of(2023, 5, 22));
	}
}
