package superapp.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.command.MiniAppCommandBoundary;


public class GetAllCommandHistory extends BaseAdminTests{
	
	@PostConstruct
	public void init() {
		super.init();
	}
	
	@Test
	public void testGetAllCommandsHistoryUsingPagination() {
		MiniAppCommandBoundary[] allCommands = createNumberOfCommands(15, "miniapp");

		MiniAppCommandBoundary[] commands =
				this.restTemplate.getForObject(
				this.urlCommand + "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}", 
				MiniAppCommandBoundary[].class,
				userAdmin.getUserId().getSuperapp(), 
				userAdmin.getUserId().getEmail(), 10,1);
		
		assertThat(commands)
		.isNotNull()
		.hasSize(5)
		.usingRecursiveFieldByFieldElementComparator()
		.isSubsetOf(allCommands);
	}
}
