package blog.services;









import blog.helpers.UserPageResponse;
import blog.interactors.UserDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;


//lIST OF METHODS OF USER SERVICE


public interface UserService {
	
	
	UserDto createAdmin(UserDto user);
	UserDto updateUser(UserDto user,Integer id);
	UserDto getUserById(Integer id);
	UserPageResponse getAllUsers(int pageNumber,int pageSize,String sortBy,String sortDir);
	void deleteUser(Integer id);
	UserDto registerUser(UserDto user) throws AddressException, MessagingException;
	
	
	
	//Clean up Disabled User Account
	
	void disabledUser(boolean flag);
	
	//Get Active Accounts
	
	UserPageResponse getAllActiveUser(int pageNumber,int pageSize,String sortBy,String sortDir);
	
	// Change Password By OLd Password
	
	boolean changePassword(String oldPassword,String newPassword,String email);
	
	//Forget Password Functionality
	
	boolean resetPassword(String email) throws AddressException, MessagingException;
	
	
	
	

}
