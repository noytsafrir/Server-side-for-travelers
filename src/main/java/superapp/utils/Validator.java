package superapp.utils;

import java.util.regex.Pattern;

public class Validator {

	public static boolean isValidEmail(String email) {
		String emailRegex = "^(.+)@(\\S+)$";
		Pattern pattern = Pattern.compile(emailRegex);
		return pattern.matcher(email).matches();
	}

}
