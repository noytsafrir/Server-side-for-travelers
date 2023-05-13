package superapp.utils;

import java.util.Optional;
import java.util.regex.Pattern;

import superapp.dal.UserCrud;
import superapp.data.UserEntity;
import superapp.data.UserPrimaryKeyId;
import superapp.data.UserRole;

public class Validator {
	
	public static boolean isValidEmail(String email) {
	    String emailRegex = "^(.+)@(\\S+)$";
	    Pattern pattern = Pattern.compile(emailRegex);
	    return pattern.matcher(email).matches();
	}
	
    public static boolean isValidUserCredentials(UserPrimaryKeyId userId, UserRole role, UserCrud repository) {
    	Optional<UserEntity> userE = repository.findById(userId);
    	return userE.isPresent() && userE.get().getRole().equals(role);
}
	
}
