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


public class GetAllCommandsHistory extends BaseAdminTests{
	
	@PostConstruct
	public void init() {
		super.init();
	}
	
	@Test
	public void testGetAllCommandsHistoryUsingPagination() {
		MiniAppCommandBoundary[] allCommands = createNumberOfCommands(15);

		MiniAppCommandBoundary[] commands =
				this.restTemplate.getForObject(
				this.urlCommand + "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}", 
				MiniAppCommandBoundary[].class,
				userAdmin.getUserId().getSuperapp(), 
				userAdmin.getUserId().getEmail(), 10,1);
		
		assertThat(commands).isNotNull().hasSize(5).usingRecursiveFieldByFieldElementComparator()
		.isSubsetOf(allCommands);
	}
	
}
