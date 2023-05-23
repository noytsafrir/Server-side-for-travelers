package superapp.logic;

import java.util.List;

import superapp.boundaries.user.UserBoundary;

public interface UsersService {
	
	public UserBoundary createUser(UserBoundary user);
	public UserBoundary login(String userSuperApp, String userEmail);
	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update);
	@Deprecated
	public List<UserBoundary> getAllUsers();
	@Deprecated
	public void deleteAllUsers();
}
