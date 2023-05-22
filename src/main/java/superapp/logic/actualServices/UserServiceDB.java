package superapp.logic.actualServices;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.user.UserId;
import superapp.converters.UserConverter;
import superapp.dal.UserCrud;
import superapp.data.UserEntity;
import superapp.data.UserPrimaryKeyId;
import superapp.data.UserRole;
import superapp.exceptions.DeprecatedException;
import superapp.exceptions.ForbbidenException;
import superapp.exceptions.InvalidInputException;
import superapp.exceptions.ResourceAlreadyExistException;
import superapp.exceptions.ResourceNotFoundException;
import superapp.logic.GeneralService;
import superapp.logic.UsersServiceNew;
import superapp.utils.Validator;

@Service // spring create an instance from this class so we can use it
public class UserServiceDB extends GeneralService implements UsersServiceNew {
	private UserCrud userCrud;
	private UserConverter userConverter;
	private String superappName;

	@Autowired // put in the parameter an instance
	public void setUserCrud(UserCrud userCrud) {
		this.userCrud = userCrud;
	}

	@Autowired
	public void setUserConverter(UserConverter userConverter) {
		this.userConverter = userConverter;
	}

	// have spring inject a configuration to this method
	@Value("${spring.application.name:defaultValue}")
	public void setSuperappName(String superappName) {
		this.superappName = superappName;
	}

	@PostConstruct // after its build but before
	public void init() {
		System.err.println("********** spring.application.name = " + this.superappName);
	}

	@Override
	@Transactional // will be atomic - part of the transaction
	public UserBoundary createUser(UserBoundary user) {
		UserId userId = user.getUserId();
		if (
			userId == null || 
			userId.getEmail() == null ||
			!Validator.isValidEmail(userId.getEmail()) ||
			user.getUsername() == null ||
			user.getUsername().isBlank() ||
			user.getAvatar() == null ||
			user.getAvatar().isBlank() ||
			user.getRole() == null ||
			!UserRole.isValid(user.getRole())
			)
			throw new InvalidInputException(user, "create user");

		user.getUserId().setSuperapp(this.superappName);
		UserPrimaryKeyId id = new UserPrimaryKeyId(userId.getSuperapp(), userId.getEmail());
		Optional<UserEntity> newUser = this.userCrud.findById(id);
		if (newUser.isPresent())
			throw new ResourceAlreadyExistException(userId, "create user");

		this.userCrud.save(this.userConverter.toEntity(user));
		return user;
	}

	@Override
	public UserBoundary login(String userSuperApp, String userEmail) {
		UserPrimaryKeyId id = new UserPrimaryKeyId(userSuperApp, userEmail);
		UserEntity user = this.userCrud.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(id, "login user"));
		return this.userConverter.toBoundary(user);
	}

	@Override
	@Transactional
	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update) {
		UserPrimaryKeyId id = new UserPrimaryKeyId(userSuperApp, userEmail);
		UserEntity existing = this.userCrud.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(id, "update user"));

		// update entity
		if (update.getAvatar() != null && !update.getAvatar().isBlank()) {
			existing.setAvatar(update.getAvatar());
		}

		if (update.getUsername() != null && !update.getUsername().isBlank()) {
			existing.setUsername(update.getUsername());
		}

		if (update.getRole() != null && UserRole.isValid(update.getRole())) {
				existing.setRole(update.getRole());
		}

		// save (UPDATE) entity to database if user was indeed updated
		this.userCrud.save(existing);
		return this.userConverter.toBoundary(existing);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers() {
		List<UserEntity> entities = this.userCrud.findAll();
		List<UserBoundary> rv = new ArrayList<>();
		for (UserEntity e : entities) {
			rv.add(this.userConverter.toBoundary(e));
		}
		return rv;
	}

	@Override
	@Deprecated
	public void deleteAllUsers() {
		throw new DeprecatedException(LocalDate.of(2023, 5, 22));
	}
	
	
	@Override
	@Transactional
	public void deleteAllUsers(String superapp, String email) {
		UserPrimaryKeyId user = new UserPrimaryKeyId(superapp, email);
		if (!isValidUserCredentials(user, UserRole.ADMIN, this.userCrud))
			throw new ForbbidenException(user.getEmail(), "delete all objects");
		this.userCrud.deleteAll();
	}
}
