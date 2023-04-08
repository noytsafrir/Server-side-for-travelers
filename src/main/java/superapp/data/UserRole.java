package superapp.data;

public enum UserRole {
	MINIAPP_USER,
	SUPERAPP_USER,
	ADMIN;
	
	public static boolean isValid(String role) {
		if(role == null)
			return false;		
		try {
			UserRole.valueOf(role);			
			return true;
		} catch(IllegalArgumentException e) {
			return false;
		}
	}

}
