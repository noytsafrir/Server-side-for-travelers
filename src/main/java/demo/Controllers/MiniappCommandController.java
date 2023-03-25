package demo.Controllers;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import boundaries.command.MiniAppCommandBoundary;
import boundaries.command.MiniAppCommandID;


@RestController
public class MiniappCommandController {

	@RequestMapping(
			path = {"/2023b.noy.tsafrir/miniapp/{miniAppName}"},
			method = {RequestMethod.POST},
			produces = {MediaType.APPLICATION_JSON_VALUE},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public Object invokeCommand(
			@PathVariable("miniAppName")String miniApp, 
			@RequestBody MiniAppCommandBoundary command) {

			command.setCommandID(new MiniAppCommandID("2023b.noy.tsafrir", miniApp, "1"));
			command.setInvocationTimestamp(new Date());
			
			return command;
	}
}
