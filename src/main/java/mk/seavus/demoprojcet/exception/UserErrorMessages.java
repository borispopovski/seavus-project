package mk.seavus.demoprojcet.exception;

public enum UserErrorMessages {
	
	USER_ID("The user with this id "),
	USER_USERNAME("The user with this username "),
	DOES_NOT_EXISTS(" doesn't exists in DB"),
	ID_USERNAME_CANT_CHANGE("Id and username can't change!");
	
	private final String message;
	
	private UserErrorMessages(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
