package superapp.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.command.MiniAppCommandBoundary;


public class GetSpecificCommandHistory extends BaseAdminTests{
	
	@PostConstruct
	public void init() {
		super.init();
	}

	@Test
	public void testGetSpecificCommandsHistoryUsingPaginationInvalidAuth() {
		String miniappToFind = "miniapp";

		createCommand(miniappToFind);

		HttpClientErrorException exSuperapp = assertThrows(HttpClientErrorException.class, ()-> {
				this.restTemplate.getForObject(
				this.urlCommand + "/" + miniappToFind+"?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}", 
				MiniAppCommandBoundary[].class,
				userSuperapp.getUserId().getSuperapp(), 
				userSuperapp.getUserId().getEmail(), 10,0);
				});
		
		assertEquals(HttpStatus.FORBIDDEN, exSuperapp.getStatusCode());
		
		HttpClientErrorException exMiniapp = assertThrows(HttpClientErrorException.class, ()-> {
			this.restTemplate.getForObject(
			this.urlCommand + "/" +miniappToFind +"?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}", 
			MiniAppCommandBoundary[].class,
			userMiniapp.getUserId().getSuperapp(), 
			userMiniapp.getUserId().getEmail(), 10,0);
			});
	
		assertEquals(HttpStatus.FORBIDDEN, exMiniapp.getStatusCode());

	}
	
}
