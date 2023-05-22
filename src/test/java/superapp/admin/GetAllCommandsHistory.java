package superapp.admin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.command.MiniAppCommandBoundary;


public class GetAllCommandsHistory extends BaseAdminTests{
	
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
	
	//TODO: getAllWithInvalidAuth
}
