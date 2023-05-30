package superapp.logic.actualServices;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.converters.MiniAppCommandConverter;
import superapp.dal.MiniAppCommandCrud;
import superapp.data.MiniAppCommandEntity;

@Component
public class AsyncCommandsHandler {
	private ObjectMapper jackson;
	private MiniAppCommandConverter commandConverter;
	private MiniAppCommandCrud commandCrud;

	@Autowired
	public AsyncCommandsHandler(MiniAppCommandConverter converter,	MiniAppCommandCrud miniAppCommandCrud) {
		this.jackson = new ObjectMapper();
		this.commandConverter = converter;
		this.commandCrud = miniAppCommandCrud;
	}

	@JmsListener(destination = "asyncCommandsQueue")
	public void handleCommandsFromQueue(String json) {
		try {
			MiniAppCommandBoundary commandBoundary = this.jackson.readValue(json, MiniAppCommandBoundary.class);

			this.handleCommand(json);

			commandBoundary.getCommandAttributes().put("status", "remote-is-done");

			MiniAppCommandEntity commandEntity = commandConverter.toEntity(commandBoundary);
			commandCrud.save(commandEntity);

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private void handleCommand(String json) {
		System.err.println("Doing something...");
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		System.err.println("Did something!");
	}
}