package superapp.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import superapp.boundaries.user.UserBoundary;
import superapp.converters.UserConverter;
import superapp.dal.UserCrud;

@Service
public class UserServiceRDB implements UsersService {
	private UserCrud userCrud;
	private UserConverter userConverter;

	@Autowired
	public void setUserCrud(UserCrud userCrud) {
		this.userCrud = userCrud;
	}
	
	@Autowired
	public void setUserConverter(UserConverter userConverter) {
		this.userConverter = userConverter;
	}

	@Override
	@Transactional
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
