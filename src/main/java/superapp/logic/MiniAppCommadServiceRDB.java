package superapp.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.converters.MiniAppCommandConverter;
import superapp.dal.MiniAppCommandCrud;
import superapp.data.MiniAppCommandEntity;
import superapp.data.MiniAppCommandPrimaryKeyId;
import superapp.data.UserEntity;

@Service
public class MiniAppCommadServiceRDB implements MiniAppCommandService {
	private MiniAppCommandCrud miniCrud;
	private MiniAppCommandConverter miniConverter;
	private String superAppName;

	@Autowired
	public void setMiniCrud(MiniAppCommandCrud miniCrud) {
		this.miniCrud = miniCrud;
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
		if (command == null || command.getCommandID() == null || command.getTargetObject() == null
				|| command.getCommandAttributes() == null || command.getInvokedBy() == null
				|| command.getCommand() == null || command.getCommandID().getMiniapp() == null
				|| command.getTargetObject().getObjectId() == null
				|| command.getTargetObject().getObjectId().getInternalObjectId() == null
				|| command.getTargetObject().getObjectId().getSuperapp() == null
				|| command.getInvokedBy().getUserId() == null || command.getInvokedBy().getUserId().getEmail() == null
				|| command.getInvokedBy().getUserId().getSuperapp() == null) {
			throw new RuntimeException("could not create a command without all the valid details");
		}
		command.getCommandID().setSuperapp(superAppName);
		command.setInvocationTimestamp(new Date());
		command.getCommandID().setInternalCommandId(System.currentTimeMillis() + "");
		MiniAppCommandPrimaryKeyId id = new MiniAppCommandPrimaryKeyId(command.getCommandID().getSuperapp(),
				command.getCommandID().getMiniapp(), command.getCommandID().getInternalCommandId());
		Optional<MiniAppCommandEntity> entity = miniCrud.findById(id);
		if (entity.isPresent()) {
			throw new RuntimeException("command already exists ");
		}
		MiniAppCommandEntity newEntity = miniConverter.toEntity(command);
		miniCrud.save(newEntity);
		return newEntity;
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

	// learn how to do this func and to do later
	@Override
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {

	}

	@Override
	public void deleteAllCommands() {
		this.miniCrud.deleteAll();
	}

}
