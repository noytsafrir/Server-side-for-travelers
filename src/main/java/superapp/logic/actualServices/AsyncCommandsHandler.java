package superapp.logic.actualServices;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.converters.MiniAppCommandConverter;
import superapp.dal.MiniAppCommandCrud;
import superapp.data.MiniAppCommandEntity;
import superapp.logic.MiniAppCommandAsyncService;

@Component
public class AsyncCommandsHandler {
	private ObjectMapper jackson;
	private MiniAppCommandConverter commandConverter;
	private MiniAppCommandCrud commandCrud;
	private MiniAppCommandAsyncService commandService;
	private final Log logger = LogFactory.getLog(MiniAppCommandServiceDB.class);


	@Autowired
	public AsyncCommandsHandler(MiniAppCommandConverter converter,	MiniAppCommandCrud miniAppCommandCrud, MiniAppCommandAsyncService commandService) {
		this.jackson = new ObjectMapper();
		this.commandConverter = converter;
		this.commandCrud = miniAppCommandCrud;
		this.commandService = commandService;
	}

	@JmsListener(destination = "asyncCommandsQueue")
	public void handleCommandsFromQueue(String json) {
		logger.debug("handleCommandsFromQueue: " + json);
		try {
			MiniAppCommandBoundary commandBoundary = this.jackson.readValue(json, MiniAppCommandBoundary.class);

			commandService.handleCommand(commandBoundary);
			commandBoundary.getCommandAttributes().put("status", "remote-is-done");

			logger.trace("Async handle command is done: " + commandBoundary);
			MiniAppCommandEntity commandEntity = commandConverter.toEntity(commandBoundary);
			commandCrud.save(commandEntity);
			logger.trace("command saved: " + commandEntity);
		} catch (Exception e) {
			logger.warn("Failed to handle command from queue: " + json);
		}
	}
}