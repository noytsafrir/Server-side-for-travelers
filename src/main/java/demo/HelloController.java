package demo;

import java.util.Random;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	private Random random;
	
	public HelloController() {
		this.random = new Random(System.currentTimeMillis());
	}
	
	@RequestMapping(
		path = {"/hello"},
		method = {RequestMethod.GET},
		produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message hello () {
		return new Message("Hello World! (" + this.random.nextInt(100)+ ")");
	}
}
