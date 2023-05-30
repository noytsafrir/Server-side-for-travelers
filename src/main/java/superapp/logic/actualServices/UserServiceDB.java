package superapp.logic.actualServices;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.object.ObjectBoundary;
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

@Service
public class UserServiceDB extends GeneralService implements UsersServiceNew {
	private UserCrud userCrud;
	private UserConverter userConverter;
	private String superappName;

	private Log logger = LogFactory.getLog(MiniAppCommandServiceDB.class);

	@Autowired
	public void setUserCrud(UserCrud userCrud) {
		this.userCrud = userCrud;
	}

	@Autowired
	public void setUserConverter(UserConverter userConverter) {
		this.userConverter = userConverter;
	}

	@Value("${spring.application.name:defaultValue}")
	public void setSuperappName(String superappName) {
		this.superappName = superappName;
	}


	@Override
	public UserBoundary createUser(UserBoundary user) {
		logger.trace("Creating user: " + user);

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
		) {
			logger.warn("Invalid input for create user: " + user);
			throw new InvalidInputException(user, "create user");
		}

		user.getUserId().setSuperapp(this.superappName);
		UserPrimaryKeyId id = new UserPrimaryKeyId(userId.getSuperapp(), userId.getEmail());
		Optional<UserEntity> newUser = this.userCrud.findById(id);
		if (newUser.isPresent()) {
			logger.warn("User already exists: " + user);
			throw new ResourceAlreadyExistException(userId, "create user");
		}

		this.userCrud.save(this.userConverter.toEntity(user));
		logger.debug("User created: " + user);
		return user;
	}

	@Override
	public UserBoundary login(String userSuperApp, String userEmail) {
		logger.trace("Login user: " + userSuperApp + " " + userEmail);

		UserPrimaryKeyId id = new UserPrimaryKeyId(userSuperApp, userEmail);
		UserEntity user = this.userCrud.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(id, "login user"));

		logger.debug("User logged in: " + user);
		return this.userConverter.toBoundary(user);
	}

	@Override
	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update) {
		logger.trace("Update user: " + userSuperApp + " " + userEmail + " " + update);
		UserPrimaryKeyId id = new UserPrimaryKeyId(userSuperApp, userEmail);
		UserEntity existing = this.userCrud.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(id, "update user"));
		logger.trace("User found: " + existing);

		// update entity
		if (update.getAvatar() != null && !update.getAvatar().isBlank()) {
			logger.trace("Updating avatar to: " + update.getAvatar() + " for user: " + existing.getUserId().getEmail());
			existing.setAvatar(update.getAvatar());
		}

		if (update.getUsername() != null && !update.getUsername().isBlank()) {
			logger.trace("Updating username to: " + update.getUsername() + " for user: " + existing.getUserId().getEmail());
			existing.setUsername(update.getUsername());
		}

		if (update.getRole() != null && UserRole.isValid(update.getRole())) {
			logger.trace("Updating role to: " + update.getRole() + " for user: " + existing.getUserId().getEmail());
			existing.setRole(update.getRole());
		}

		// save (UPDATE) entity to database if user was indeed updated
		this.userCrud.save(existing);
		logger.debug("User updated: " + existing);
		return this.userConverter.toBoundary(existing);
	}

	@Override
	public List<UserBoundary> getAllUsers(String superapp, String email, int size, int page) {
		boolean isAdmin;
		logger.trace("Get all users for user: " + superapp + " " + email);
		List<UserBoundary> users;
		UserEntity user = getUser(new UserPrimaryKeyId(superapp, email), userCrud);
		isAdmin = user.getRole().equals(UserRole.ADMIN.toString());

		if(!isAdmin) {
			logger.warn("User: " + user.getUserId().getEmail() + " is not admin");
			throw new ForbbidenException(user.getUserId().getEmail(), "get all users");
		}

		users = this.userCrud.findAll(PageRequest.of(page, size, Direction.DESC, "role", "userId"))
				.stream()
				.map(this.userConverter :: toBoundary)
				.toList();

		logger.trace("All users returned (" + users.size() + " objects)");
		return users;
	}

	@Override
	public void deleteAllUsers(String superapp, String email) {
		logger.trace("Delete all users by user: " + superapp + " " + email);
		UserPrimaryKeyId user = new UserPrimaryKeyId(superapp, email);
		if (!isValidUserCredentials(user, UserRole.ADMIN, this.userCrud))
			throw new ForbbidenException(user.getEmail(), "delete all objects");
		this.userCrud.deleteAll();
		logger.info("All users deleted");
	}

	@Override
	@Deprecated
	public void deleteAllUsers() {
		throw new DeprecatedException(LocalDate.of(2023, 5, 22));
	}

	@Override
	@Deprecated
	public List<UserBoundary> getAllUsers() {
		throw new DeprecatedException(LocalDate.of(2023, 5, 30));
	}

}
