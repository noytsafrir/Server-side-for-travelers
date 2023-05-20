package superapp.logic;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

import superapp.dal.UserCrud;
import superapp.data.UserEntity;
import superapp.data.UserPrimaryKeyId;
import superapp.data.UserRole;
import superapp.exceptions.ResourceNotFoundException;

public abstract class GeneralService {
	protected String superappName;

	@Value("${spring.application.name:defaultValue}")
	public void setSuperappName(String superappName) {
		this.superappName = superappName;
	}
	
	public String getSuperappName() {
		return superappName;
	}

	public boolean isValidUserCredentials(UserPrimaryKeyId userId, UserRole role, UserCrud repository) {
		return getUser(userId, repository).getRole().toString().equals(role.toString());
	}
	
	public UserEntity getUser(UserPrimaryKeyId userId, UserCrud repository) {
		Optional<UserEntity> userE = repository.findById(userId);
		if (!userE.isPresent())
			throw new ResourceNotFoundException(userId, "validate user role");
		return userE.get();
	}
}
