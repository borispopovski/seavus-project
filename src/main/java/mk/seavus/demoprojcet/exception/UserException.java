package mk.seavus.demoprojcet.exception;

public class UserException extends RuntimeException {
	
	private static final long serialVersionUID = 6726363367325704235L;
	
	public UserException(String message) {
		super(message);
	}
	
	public UserException(String arg1, String arg2, String arg3) {
		super(arg1 + arg2 + arg3);
	}

}
