package superapp.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.object.ObjectBoundary;


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
		
//		assertThat(getCommandsHistory())
//		.isNotNull()
//		.hasSize(0);
	}
	
//	public MiniAppCommandBoundary[] getCommandsHistory() {
//
//		MiniAppCommandBoundary[] allCommands =
//				this.restTemplate.getForObject(
//				this.urlCommand + "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}", 
//				MiniAppCommandBoundary[].class,
//				userAdmin.getUserId().getSuperapp(), 
//				userAdmin.getUserId().getEmail());
//		return allCommands;
//	}
	
	
}
