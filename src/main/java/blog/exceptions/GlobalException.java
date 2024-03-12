package blog.exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import blog.helpers.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.SendFailedException;

@ControllerAdvice
public class GlobalException {
	
	// HANDLER METHOD FOR RESOUCE NOT FOUND EXCEPTION
	
	@ExceptionHandler(ResourceNotFound.class)
public ResponseEntity<ApiResponse> resouceNotFoundExceptionHandler(ResourceNotFound exception){
		String message=exception.getMessage();
		ApiResponse response=new ApiResponse(message,false);
		
		return new ResponseEntity<ApiResponse>(response,HttpStatus.NOT_FOUND);
}
	
	// HANDLER METHOD FOR METHOD ARGUMENT NOT VALID EXCEPTION
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> NotValidException(MethodArgumentNotValidException e){
		Map<String, String> errorResponse=new HashMap<>();
		e.getBindingResult().getAllErrors().forEach((error)->{
			String fieldName=((FieldError)error).getField();
			String errorMessge=error.getDefaultMessage();
			
			errorResponse.put(fieldName, errorMessge);
			
		});	
				return new ResponseEntity<Map<String,String>>(errorResponse,HttpStatus.BAD_REQUEST);
	}
	
	
	
	//HANDLER METHOD OF IO EXPECTION
	
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<ApiResponse> ioException(IOException e){	
		return new ResponseEntity<>(new ApiResponse("Uploading Error: "+e.getMessage(), false),HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	//HANDLER METHOD OF FILE NOT FOUND EXCEPTION
	
	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<ApiResponse> imageNotException(FileNotFoundException e){	
		return new ResponseEntity<>(new ApiResponse("Error: "+e.getMessage(), false),HttpStatus.NOT_FOUND);
		
	}
	
	//HANDLER METHOD OF BAD CREDENTIAL EXCEPTION
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiResponse> credentialError(BadCredentialsException e){
		String message="Credentials Invalid !!";
		ApiResponse response=new ApiResponse(message,false);
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	//E-mail Unique constraints Handler
	
	@ExceptionHandler(java.sql.SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<ApiResponse> duplicateError(java.sql.SQLIntegrityConstraintViolationException e){
		String message=e.getMessage();
		ApiResponse response=new ApiResponse(message,false);
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	
	//Expired Token Handler
	
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ApiResponse> expiredTokenError(ExpiredJwtException e){	
		String message="Token Expired!!";
		ApiResponse response=new ApiResponse(message,false);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	//E-mail confirmation token related exception handler
	
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ApiResponse> confirmationTokenHandler(IllegalStateException e){
		String message=e.getMessage();
		ApiResponse response=new ApiResponse(message,false);
		
		return new ResponseEntity<>(response,HttpStatus.NOT_ACCEPTABLE);
	
		
	}
	
	//User Not Found HANDLER METHOD
	
	
	@ExceptionHandler(UserNotFound.class)
public ResponseEntity<ApiResponse> UserNotFoundExceptionHandler(UserNotFound exception){
		String message=exception.getMessage();
		ApiResponse response=new ApiResponse(message,false);
		
		return new ResponseEntity<ApiResponse>(response,HttpStatus.NOT_FOUND);
}
	
	
	//Missing value in parameter Handler method
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
public ResponseEntity<ApiResponse> MissingValueExceptionHandler(MissingServletRequestParameterException exception){
		String message=exception.getMessage();
		ApiResponse response=new ApiResponse(message,false);
		
		return new ResponseEntity<ApiResponse>(response,HttpStatus.NOT_FOUND);
}
	
	@ExceptionHandler(SendFailedException.class)
	public ResponseEntity<ApiResponse> sendMailExceptionHandler(SendFailedException exception){
		String message=exception.getMessage();
		ApiResponse response=new ApiResponse(message,false);
		
		return new ResponseEntity<ApiResponse>(response,HttpStatus.NOT_FOUND);
}
	
	
}


	
	

