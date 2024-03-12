package blog.serviceClass;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import blog.config.AppConstants;
import blog.entities.EmailConfirmationToken;
import blog.entities.Post;
import blog.entities.Roles;
import blog.entities.User;
import blog.exceptions.ResourceNotFound;
import blog.exceptions.UserNotFound;
import blog.helpers.EmailSender;
import blog.helpers.UserExchanger;
import blog.helpers.UserPageResponse;
import blog.interactors.UserDto;
import blog.repositories.EmailConfirmationTokenRepo;
import blog.repositories.RoleRepo;
import blog.repositories.UserRepo;
import blog.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
@Service
public class UserServiceClass implements UserService{
	
	//reference of necessary class used in this class 
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserExchanger exchanger;
	
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private ConfirmationTokenServiceClass tokenServiceClass;
	@Autowired
	private EmailSender sender;
	@Autowired
	private FileServiceClass fileServiceClass;
	@Autowired
	private EmailConfirmationTokenRepo confirmationTokenRepo;
	
	@Value("${project.image}")
	String path;
	
	

	//CREATE ADMIN SERVICE
	
	@Override
	public UserDto createAdmin(UserDto user) {
		User adminUser=this.exchanger.userExchanger(user);
		adminUser.setPassword(this.encoder.encode(user.getPassword()));
		Roles role=this.roleRepo.findById(AppConstants.ADMIN).get();
		adminUser.getRoles().add(role);
		adminUser.setEnabled(true);
		User registeredAdmin=this.userRepo.save(adminUser);
	
		return this.exchanger.userDtoExchanger(registeredAdmin);
	}

	//UPDATE USER SERVICE
	
	@Override
	public UserDto updateUser(UserDto user, Integer id) {
		User getUser=this.userRepo.findById(id).orElseThrow(()->new ResourceNotFound("User","id" , id));
		getUser.setAbout(user.getAbout());
		getUser.setName(user.getName());
        User updatedUser=this.userRepo.save(getUser);
		return this.exchanger.userDtoExchanger(updatedUser);
	}
	
	//GET USER BY ID

	@Override
	public UserDto getUserById(Integer id) {
		
		User findUser=this.userRepo.findById(id).orElseThrow(()->new ResourceNotFound("User","id" , id));
		return this.exchanger.userDtoExchanger(findUser);
	}
	
	//GET ALL USER SERVICE

	@Override
	public UserPageResponse getAllUsers(int pageNumber,int pageSize,String sortBy,String sortDir) {
		Sort sort=null;
		if(sortDir.equalsIgnoreCase(AppConstants.SORT_DIR)) {
			sort=Sort.by(sortBy).ascending();
		}else {
			sort=Sort.by(sortBy).descending();
		}
		
		Pageable pageable =PageRequest.of(pageNumber, pageSize,sort);
		Page<User> userPage=this.userRepo.findAll(pageable);
		List<User> allUsers=userPage.getContent();
		List<UserDto> userDTOList=allUsers.stream().map((obj)->this.exchanger.userDtoExchanger(obj)).toList();
		UserPageResponse response=new UserPageResponse();
		response.setContent(userDTOList);
		response.setLastPage(userPage.isLast());
		response.setPageNumber(userPage.getNumber());
		response.setPageSize(userPage.getSize());
		response.setTotalElemnets(userPage.getTotalElements());
		response.setTotalPages(userPage.getTotalPages());
		
		return response;
	}
	
	//DELETE USER SERVICE

	@Override
	public void deleteUser(Integer id) {
		User deleteUser=this.userRepo.findById(id).orElseThrow(()->new ResourceNotFound("User","id" , id));
		this.tokenServiceClass.deleteAllConfirmationToken(deleteUser);
		deleteUser.setRoles(null);
		this.userRepo.save(deleteUser);
		List<Post> listOfPosts=deleteUser.getListOfPosts();
		
		for(Post deletePost:listOfPosts) {
			
			this.fileServiceClass.deleteFile(deletePost.getImageName(), path);
		}
		
		
		
		
		
		
	  this.userRepo.delete(deleteUser);
		
	}
	
	//REGISTER NEW USER API

	@Override
	public UserDto registerUser(UserDto user) throws AddressException, MessagingException {
		String to=user.getEmail();
		String from=AppConstants.ADMIN_ACCOUNT_EMAIL;
		
		//welcome user in the blog application
		
		String welcomeSubject=AppConstants.WELCOME_SUBJECT;
		String welcomeText=AppConstants.WELCOME_TEXT;

		//Email Send
		
		this.sender.sendEmail(to, from, welcomeSubject, welcomeText);
		
		User registerUser=this.exchanger.userExchanger(user);
		registerUser.setPassword(this.encoder.encode(user.getPassword()));
		Roles role=this.roleRepo.findById(AppConstants.USER).get();
		registerUser.getRoles().add(role);
		User registeredUser=this.userRepo.save(registerUser);
         List<EmailConfirmationToken> tokens=this.confirmationTokenRepo.findByUser(registeredUser);
         if(tokens.size()>5) {
        	 registerUser.setLocked(true);
        	 this.userRepo.save(registeredUser);
        	 return null;
         }
		//Generation of Email Confirmation Token 
		String generatedToken=this.tokenServiceClass.generateSaveToken(registeredUser.getEmail(),2);
		String subject=AppConstants.SUBJECT;
		String text=AppConstants.TEXT+generatedToken;
		//Email Send
		this.sender.sendEmail(to, from, subject, text);
		return this.exchanger.userDtoExchanger(registeredUser);
		
	}
	
//	 Clean up disable accounts.

	
	
	@Override
	public void disabledUser(boolean flag) {
		List<User> disabledUsers=this.userRepo.findByEnabled(flag);
		
		for(User user:disabledUsers) {
			this.tokenServiceClass.deleteAllConfirmationToken(user);
	   this.userRepo.delete(user);
		}
		
	}
	
	//Get All Active Accounts.

	@Override
	public UserPageResponse getAllActiveUser(int pageNumber,int pageSize,String sortBy,String sortDir) {
		Sort sort=null;
		if(sortDir.equalsIgnoreCase(AppConstants.SORT_DIR)) {
			sort=Sort.by(sortBy).ascending();
		}else {
			sort=Sort.by(sortBy).descending();
		}
		
		Pageable pageable =PageRequest.of(pageNumber, pageSize,sort);
		Page<User> getActiveUsers=this.userRepo.findByEnabled(true,pageable);
		List<User> allUsers=getActiveUsers.getContent();
		List<UserDto>listOfActiveDtos=allUsers.stream().map((obj)->this.exchanger.userDtoExchanger(obj)).toList();
		UserPageResponse response=new UserPageResponse();
		response.setContent(listOfActiveDtos);
		response.setLastPage(getActiveUsers.isLast());
		response.setPageNumber(getActiveUsers.getNumber());
		response.setPageSize(getActiveUsers.getSize());
		response.setTotalElemnets(getActiveUsers.getTotalElements());
		response.setTotalPages(getActiveUsers.getTotalPages());
		return response;
	}
	
	
	// Change Password By OLd Password

	@Override
	public boolean changePassword(String oldPassword, String newPassword,String email) {
		User user=this.userRepo.findByEmail(email).orElseThrow(()->new UserNotFound("User", "email", email));
		if(encoder.matches(oldPassword,user.getPassword())) {
			user.setPassword(this.encoder.encode(newPassword));
			this.userRepo.save(user);
			return true;
		} else {
			throw new IllegalStateException("Password is Incrrect,Try again!!");
		}
	}

	
	//Reset Password(Forget Password Functionality)
	
	@Override
	public boolean resetPassword(String email) throws AddressException, MessagingException {
		User user=this.userRepo.findByEmail(email).orElseThrow(()->new UserNotFound("User", "email", email));
		if(!user.isEnabled()) {
			return false;
		}
		List<EmailConfirmationToken> tokens=this.confirmationTokenRepo.findByUser(user);
        if(tokens.size()>5) {
        	user.setLocked(true);
        	this.userRepo.save(user);
       	 return false;
        }
		String token=this.tokenServiceClass.generateSaveToken(email, 10);
		String generateUrl=AppConstants.BASE_URL+AppConstants.END_POINT+"?token="+token;
		this.sender.sendEmail(email, AppConstants.ADMIN_ACCOUNT_EMAIL,AppConstants.SUBJECT_FORGOT_EMAIL_STRING ,"The Rest Password Link : "+ generateUrl);
		return true;
	}
	
	
	
	
	
	

	

}
