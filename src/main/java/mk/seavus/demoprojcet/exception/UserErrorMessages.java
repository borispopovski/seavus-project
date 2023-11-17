package mk.seavus.demoprojcet.exception;

public enum UserErrorMessages {
	
	USER_ID("The user with this id '"),
	USER_USERNAME("The user with this username '"),
	DOES_NOT_EXISTS("' doesn't exists in DB"),
	USERNAME_CANT_CHANGE("Username can't change!"),
	EXISTS("' exists in DB");
	
	private final String message;
	
	private UserErrorMessages(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
