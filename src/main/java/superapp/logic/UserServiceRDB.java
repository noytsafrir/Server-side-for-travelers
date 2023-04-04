package superapp.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.user.UserBoundary;
import superapp.converters.UserConverter;
import superapp.dal.UserCrud;

@Service //spring create an instance from this class so we can use it
public class UserServiceRDB implements UsersService {
	private UserCrud userCrud;
	private UserConverter userConverter;
	private String nameFromSpringConfig;

	@Autowired //put in the parameter an instance
	public void setUserCrud(UserCrud userCrud) {
		this.userCrud = userCrud;
	}
	
	@Autowired
	public void setUserConverter(UserConverter userConverter) {
		this.userConverter = userConverter;
	}

// ********************************* START NEW FROM CLASS *********************************************** 
	//have spring inject a configuration to this method
	@Value("${spring.application.name:defaultValue}")
	public void setNameFromSpringConfig(String nameFromSpringConfig) {
		this.nameFromSpringConfig = nameFromSpringConfig;
	}


	@PostConstruct //after its build but before
	public void init() {
		System.err.println("********** spring.application.name = "+this.nameFromSpringConfig);
	}
	
	
// ********************************* END FROM CLASS ****************************************************** 

	
	@Override
	@Transactional //will be atomic - part of the transaction 
	public UserBoundary createUser(UserBoundary user) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public UserBoundary login(String userSuperApp, String userEmail) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserBoundary> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllUsers() {
		// TODO Auto-generated method stub
		
	}

}
