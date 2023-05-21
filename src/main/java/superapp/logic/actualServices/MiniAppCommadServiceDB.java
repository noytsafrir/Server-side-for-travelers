package superapp.logic.actualServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import superapp.exceptions.ForbbidenException;
import superapp.exceptions.InvalidInputException;
import superapp.exceptions.ResourceAlreadyExistException;
import superapp.exceptions.ResourceNotFoundException;
import superapp.logic.GeneralService;
import superapp.logic.MiniAppCommandAsyncService;
import superapp.utils.Validator;


@Service
public class MiniAppCommadServiceDB extends GeneralService implements MiniAppCommandAsyncService {
	private MiniAppCommandCrud miniCrud;
    private ObjectCrud objects;
    private UserCrud users;
	private MiniAppCommandConverter miniConverter;
	private String superAppName;
	private JmsTemplate jmsTemplate;
	private ObjectMapper jackson;


	@Autowired
	public void setMiniCrud(MiniAppCommandCrud miniCrud) {
		this.miniCrud = miniCrud;
	}

	@Autowired
	public void setObjects(ObjectCrud objects) {
		this.objects = objects;
	}

	@Autowired
	public void setUsers(UserCrud users) {
		this.users = users;
	}

	@Autowired
	public void setMiniConverter(MiniAppCommandConverter miniConverter) {
		this.miniConverter = miniConverter;
	}

	@Value("${spring.application.name:defaultValue}")
	public void setSuperAppName(String superAppName) {
		this.superAppName = superAppName;
	}
	
	@Override
	public Object invokeCommand(MiniAppCommandBoundary command) {
		// Check command validity and throw appropriate exception if not valid:
		checkInvokedCommand(command, UserRole.MINIAPP_USER);

		command.setInvocationTimestamp(new Date());
		command.getCommandId().setSuperapp(superAppName);
		command.getCommandId().setInternalCommandId(UUID.randomUUID().toString());
		MiniAppCommandPrimaryKeyId id = new MiniAppCommandPrimaryKeyId(command.getCommandId().getSuperapp(),
				command.getCommandId().getMiniapp(), command.getCommandId().getInternalCommandId());
		
		Optional<MiniAppCommandEntity> entity = miniCrud.findById(id);
		if (entity.isPresent()) {
			throw new ResourceAlreadyExistException(id, "invoke command");
		}
		
		MiniAppCommandEntity newEntity = miniConverter.toEntity(command);
		miniCrud.save(newEntity);
		return newEntity;
	}

	// TODO: write this function (async logic)
	@Override
	public Object invokeCommandAsync(MiniAppCommandBoundary command) {
		command.setInvocationTimestamp(new Date());
		command.getCommandId().setSuperapp(superAppName);
		command.getCommandId().setInternalCommandId(UUID.randomUUID().toString());
//		if (command.getData() == null) {
//			message.setData(new HashMap<>());
//		}
//		message.getData().put("status", "waiting...");
		
		try {
			String json = this.jackson
				.writeValueAsString(command);
			
			this.jmsTemplate
				.convertAndSend("asyncMessageQueue", json);
			
			return command;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<MiniAppCommandBoundary> getAllCommands() {
		List<MiniAppCommandEntity> entities = this.miniCrud.findAll();
		List<MiniAppCommandBoundary> rv = new ArrayList<>();
		for (MiniAppCommandEntity m : entities) {
			rv.add(this.miniConverter.toBoundary(m));
		}
		return rv;
	}


	@Override
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {
		
		List<MiniAppCommandEntity> entities = this.miniCrud.findAll();
		List<MiniAppCommandBoundary> rv = new ArrayList<>();
		for (MiniAppCommandEntity m : entities) {
			if (m.getCommandID().getMiniapp().equals(miniAppName))
				rv.add(this.miniConverter.toBoundary(m));
		}
		return rv ;
	}

	@Override
	public void deleteAllCommands() {
		this.miniCrud.deleteAll();
	}
	
    private void checkInvokedCommand(MiniAppCommandBoundary command, UserRole userRole){
		if (command == null)
			throw new InvalidInputException(command, "command", "invoke command");
		
		if (command.getCommandId() == null)
			throw new InvalidInputException(command.getCommandId(), "command id", "invoke command");
		
		if (command.getCommandId().getMiniapp() == null || command.getCommandId().getMiniapp().isBlank())
			throw new InvalidInputException(command.getCommandId().getMiniapp(), "miniapp", "invoke command");
		
		if (command.getCommandAttributes() == null)
			throw new InvalidInputException(command.getCommandAttributes(), "command attributes", "invoke command");
				
        if (command.getCommand() == null || command.getCommand().isBlank())
			throw new InvalidInputException(command, "command string", "invoke command");

        InvocationUser invokedBy = command.getInvokedBy();
        if (invokedBy == null || invokedBy.getUserId() == null)
			throw new InvalidInputException(invokedBy, "user data", "invoke command");
        
        if (invokedBy.getUserId().getSuperapp() == null || invokedBy.getUserId().getSuperapp().isBlank())
			throw new InvalidInputException(invokedBy.getUserId().getSuperapp(), "user superapp", "invoke command");
        
        if (invokedBy.getUserId().getEmail() == null ||
        	invokedBy.getUserId().getEmail().isBlank() ||
        	!Validator.isValidEmail(invokedBy.getUserId().getEmail()))
			throw new InvalidInputException(invokedBy.getUserId().getEmail(), "user email", "invoke command");

       UserPrimaryKeyId user = new UserPrimaryKeyId(invokedBy.getUserId().getSuperapp(), invokedBy.getUserId().getEmail());
       if(!isValidUserCredentials(user, userRole, this.users))
            throw new ForbbidenException(user.getEmail(), "invoke command");

        TargetObject targetObject = command.getTargetObject();
        if (targetObject == null ||
                targetObject.getObjectId() == null ||
                targetObject.getObjectId().getSuperapp() == null ||
                targetObject.getObjectId().getInternalObjectId() == null ||
                targetObject.getObjectId().getSuperapp().isBlank() ||
                targetObject.getObjectId().getInternalObjectId().isBlank()) {
			throw new InvalidInputException(command, "target object", "invoke command");
        }

        ObjectPrimaryKeyId objId = new ObjectPrimaryKeyId(	targetObject.getObjectId().getSuperapp(),
        													targetObject.getObjectId().getInternalObjectId());
        Optional<ObjectEntity> objectE = this.objects.findById(objId);
        if(objectE.isEmpty() || !objectE.get().getActive())
            throw new ResourceNotFoundException(objId, "invoke command");
    }
}