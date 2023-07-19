package mk.seavus.demoprojcet.exception;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerMapping;

@ControllerAdvice
public class DemoProjectExceptionHandler {
	
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@ExceptionHandler({ UserException.class })
	public ResponseEntity<ErrorResponseModel> handlerRuntimeException(Exception exception, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(buildErrorResponseModel(exception, request, status), status);
	}
	
	private ErrorResponseModel buildErrorResponseModel(Exception exception, HttpServletRequest request, HttpStatus status) {
	    return ErrorResponseModel.builder()
	        .status(Integer.parseInt(status.toString().split(" ")[0]))
	        .error(status.toString().split(" ")[1])
	        .message(exception.getMessage())
	        .path(String.valueOf(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)))
	        .date(getCurrentTime())
	        .build();
	  }
	
	private String getCurrentTime() {
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
	    return sdf.format(System.currentTimeMillis());
	  }

}
