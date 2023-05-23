package superapp.logic;

import java.util.List;

import superapp.boundaries.user.UserBoundary;

public interface UsersServiceNew extends UsersService {

	public void deleteAllUsers(String superapp, String email);
	public List<UserBoundary> getAllUsers(String superapp, String email, int size, int page);
}
