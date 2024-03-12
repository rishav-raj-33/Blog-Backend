package blog.exceptions;

import lombok.Getter;
import lombok.Setter;

//USER NOT FOUND EXCEPTION

@SuppressWarnings("serial")
@Getter
@Setter
public class UserNotFound extends RuntimeException {
	
private String resouceName;
private	String fieldValue;
private	String fieldName;
	
	public UserNotFound(String resouceName, String fieldName, String fieldValue) {
		super(String.format("%s not found with %s :%s",resouceName,fieldName,fieldValue));
		this.resouceName = resouceName;
		this.fieldValue = fieldValue;
		this.fieldName = fieldName;
	}

}
