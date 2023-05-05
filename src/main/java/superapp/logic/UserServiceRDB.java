package superapp.logic;

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
import superapp.exceptions.ResourceAlreadyExistException;

@Service // spring create an instance from this class so we can use it
public class UserServiceRDB implements UsersService {
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
		// TODO have the database define the id, instead of the server or find another
		// mechanisms to generate the object
		UserId userId = user.getUserId();
		if (userId == null || userId.getEmail() == null || !(UserRole.isValid(user.getRole()))
				|| user.getUsername() == null || user.getAvatar() == null)
			throw new RuntimeException("could not create a user without all the valid details");

		user.getUserId().setSuperapp(this.superappName);
		UserPrimaryKeyId id = new UserPrimaryKeyId(userId.getSuperapp(), userId.getEmail());
		Optional<UserEntity> newUser = this.userCrud.findById(id);
		if (newUser.isPresent())
			throw new ResourceAlreadyExistException("User "+ userId+" is already exist");

		this.userCrud.save(this.userConverter.toEntity(user));
		return user;
	}

	@Override
	public UserBoundary login(String userSuperApp, String userEmail) {
		UserPrimaryKeyId id = new UserPrimaryKeyId(userSuperApp, userEmail);
		UserEntity user = this.userCrud.findById(id).orElseThrow(
				() -> new ResourceAlreadyExistException(id, "login user"));
		return this.userConverter.toBoundary(user);
	}

	@Override
	@Transactional
	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update) {
		UserPrimaryKeyId id = new UserPrimaryKeyId(userSuperApp, userEmail);
		UserEntity existing = this.userCrud.findById(id).orElseThrow(
				() -> new ResourceAlreadyExistException(id, "update user"));

		// update entity
		if (update.getAvatar() != null) {
			existing.setAvatar(update.getAvatar());
		}

		if (update.getUsername() != null) {
			existing.setUsername(update.getUsername());
		}

		if (update.getRole() != null) {
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
	@Transactional
	public void deleteAllUsers() {
		this.userCrud.deleteAll();
	}
}
