package superapp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.command.MiniAppCommandID;
import superapp.logic.MiniAppCommandAsyncService;

@RestController
public class MiniappCommandController {


	private MiniAppCommandAsyncService service;

	@Autowired

	public void setService(MiniAppCommandAsyncService service) {
		this.service = service;
	}
	
	@RequestMapping(path = { "/superapp/miniapp/{miniAppName}" }, method = { RequestMethod.POST }, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public Object invokeCommand(@PathVariable("miniAppName") String miniApp,
			@RequestParam(name = "async", required = false, defaultValue = "false") boolean async,
			@RequestBody MiniAppCommandBoundary command) {
		MiniAppCommandID cId = new MiniAppCommandID();
		cId.setMiniapp(miniApp);
		command.setCommandId(cId);
		
		if (async)
			return service.invokeCommandAsync(command);
		else
			return this.service.invokeCommand(command);
	}
}