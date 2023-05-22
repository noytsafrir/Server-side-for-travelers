package superapp.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.command.MiniAppCommandBoundary;



public class DeleteTests extends BaseAdminTests{
	
	@PostConstruct
	public void init() {
		super.init();
	}
	
	@Test
	public void testDeleteAllObjects() {
		createNumberOfObjects(20);
		this.restTemplate.delete(this.urlObject +"?userSuperapp={superapp}&userEmail={email}",
				userAdmin.getUserId().getSuperapp(), userAdmin.getUserId().getEmail());
		
		assertThat(getAllObjects())
		.isNotNull()
		.hasSize(0);
	}
	
	
	@Test
	public void deleteAllObjectsSuperappUser() {
		HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, () -> {
		createNumberOfObjects(20);
		this.restTemplate.delete(this.urlObject +"?userSuperapp={superapp}&userEmail={email}",
				userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());
		});
		assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
	}
	
	@Test
	public void testDeleteAllObjectsMiniappUser() {
		HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, () -> {
		createNumberOfObjects(20);
		this.restTemplate.delete(this.urlObject +"?userSuperapp={superapp}&userEmail={email}",
				userMiniapp.getUserId().getSuperapp(), userMiniapp.getUserId().getEmail());
		});
		assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
	}
	
	@Test
	public void testDeleteAllCommandsHistory() {
		createNumberOfCommands(20);
		this.restTemplate.delete(this.urlCommand +"?userSuperapp={superapp}&userEmail={email}",
				userAdmin.getUserId().getSuperapp(), userAdmin.getUserId().getEmail());
		
		assertThat(getCommandsHistory())
		.isNotNull()
		.hasSize(0);
	}
	
	public MiniAppCommandBoundary[] getCommandsHistory() {
		
		MiniAppCommandBoundary[] allCommands =
				this.restTemplate.getForObject(
				this.urlCommand + "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}", 
				MiniAppCommandBoundary[].class,
				userAdmin.getUserId().getSuperapp(), 
				userAdmin.getUserId().getEmail());
		return allCommands;
	}
//	
//	public MiniAppCommandBoundary createCommand() {
//		ObjectBoundary obj = createObject();
//		objs.createObject(obj);
//		MiniAppCommandBoundary newCommand = new MiniAppCommandBoundary();
//		MiniAppCommandID commandId = new MiniAppCommandID(this.superAppName, "miniapp", "test");
//		HashMap<String, Object> details = new HashMap<String, Object>();
//
//		newCommand.setCommandId(commandId);
//		newCommand.setCommand("command");
//		newCommand.setTargetObject(new TargetObject(obj.getObjectId()));
//		newCommand.setInvocationTimestamp(new Date());
//		newCommand.setInvokedBy(new InvocationUser(userMiniapp.getUserId()));
//		newCommand.setCommandAttributes(details);
//		System.err.println(newCommand.toString());
//		return newCommand;
//	}
//	
}
