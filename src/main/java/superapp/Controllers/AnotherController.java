package demo.Controllers;

import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demo.Message;

@RestController
public class AnotherController {

	@RequestMapping(
			path = {"/hello/{firstName}/{lastName}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})

	public Message hello(
			@PathVariable("firstName")String superapp , 
			@PathVariable("lastName") String lastName) {
		Message rv = new Message("hi");
		rv.setId(12L);		
		return rv;

	}
	
	@RequestMapping(
			path = {"/hello/{id}"},
			method = {RequestMethod.PUT},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public void updateSpeceficMessage(
			@PathVariable("id") Long id,
			@RequestBody Message update) {
		System.err.println("id: "+ id);
		System.err.println("update: "+ update);
	}
	
	@RequestMapping(
			path = {"/hello/{messageId}"},
			method = {RequestMethod.DELETE})
		public void deleteMessageById(
				@PathVariable("messageId") String messageId) {
			//do nothing
		
	}
	
	@RequestMapping(
			path = {"/hello"},
			method = {RequestMethod.POST},
			produces = {MediaType.APPLICATION_JSON_VALUE},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public Message createMessage(
			@RequestBody Message message) {
		message.setId((long)42);
		message.setCreatedOn(new Date());
		return message;
	}
}


