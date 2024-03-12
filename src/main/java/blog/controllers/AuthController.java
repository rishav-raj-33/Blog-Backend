package blog.controllers;



import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import blog.config.AppConstants;
import blog.entities.User;
import blog.exceptions.UserNotFound;
import blog.helpers.ApiResponse;
import blog.helpers.EmailSender;
import blog.helpers.UserExchanger;
import blog.interactors.JwtRequest;
import blog.interactors.JwtResponse;
import blog.interactors.UserDto;
import blog.repositories.UserRepo;
import blog.security.JwtHelper;
import blog.serviceClass.ConfirmationTokenServiceClass;
import blog.serviceClass.UserServiceClass;
import blog.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.validation.Valid;


//Authentication Class


@Tag(name = "AuthController",description = "API for Authentication")
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	
	//Logger for display the log on the console

    @SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    //Necessary Reference of class used in this class
	
	  @Autowired
	    private UserDetailsService userDetailsService;

	    @Autowired
	    private AuthenticationManager manager;
	    
	    @Autowired
	    private JwtHelper helper;
	    @Autowired
	    private UserService service;
	    
	    @Autowired
	 	private UserRepo userRepo;
	    
	    @Autowired
	    private UserExchanger exchanger;
	    
	    @Autowired
	    private ConfirmationTokenServiceClass confirmationService;
	    @Autowired
	    private EmailSender sender;
	    
	    @Autowired
	    private PasswordEncoder encoder;
	    
	    @Autowired
	    private UserServiceClass userService;
	    
	   
	    
	    
	    
	    
	   //Login API 
	
	
	@PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
		
		User user=this.userRepo.findByEmail(request.getEmail()).orElseThrow(()->new UserNotFound("User", "Email", request.getEmail()));
		
		if(user.isEnabled()) {
        this.doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .userName(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    } else {
    	 return new ResponseEntity<JwtResponse>(new JwtResponse("Not Generated,Verify the User Email First",request.getEmail()),HttpStatus.BAD_REQUEST);
	}
	}
	
	//JWt Token verification

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);


        } catch (BadCredentialsException e) {
        	 throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }
    
    
    //Register New User API
    
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto) throws AddressException, MessagingException{
    	this.service.registerUser(userDto);
    	return new ResponseEntity<String>("Email Send Verify Email to Enable The Account",HttpStatus.CREATED);
    }
    
    
 // Get logged-in user data
 	
 	

 	@GetMapping("/current-user/")
 	public ResponseEntity<UserDto> getUser(Principal principal) {
 		User user = this.userRepo.findByEmail(principal.getName()).get();
 		return new ResponseEntity<UserDto>(this.exchanger.userDtoExchanger(user),HttpStatus.OK);

    
        }
 	
 	

 	//Reset Password API (Forgot Password Feature)

 	   @PostMapping("/sendVerifyLink")
 	   public ResponseEntity<ApiResponse> forgotPassword(@RequestParam(required = true) String email) throws AddressException, MessagingException{
 		   this.userService.resetPassword(email);
 		return new ResponseEntity<ApiResponse>(new ApiResponse("Reset Link Send on Registered Email", true),HttpStatus.OK);
 	}
 	   
 	   
 	   
 
 	   
 	   @PostMapping("/verifyToken")
 	   public ResponseEntity<ApiResponse> resetPassword(@RequestParam(required = true) String newPassword,
 			  @RequestParam(required = true) String token,@RequestParam(required = true) String email){
 		  this.confirmationService.verifyToken(token);
 		   User user=this.userRepo.findByEmail(email).orElseThrow(()->new UserNotFound("User","Email" ,email));
 		   user.setPassword(this.encoder.encode(newPassword));
 		   this.userRepo.save(user);
 		   return new ResponseEntity<ApiResponse>(new ApiResponse("Password Updated", true),HttpStatus.ACCEPTED);
 		   
 	   }



 		// Confirm Email API
 		
 		@PostMapping("/confirm")
 		public ResponseEntity<ApiResponse> confirmEmail(@RequestParam(required = true) String confirmationToken){
 			this.confirmationService.ConfirmToken(confirmationToken);
 			return new ResponseEntity<ApiResponse>(new ApiResponse("E-mail Verified", true),HttpStatus.ACCEPTED);
 		}
 		
 		//re Send token API
 		
 		@PostMapping("/resendEmailVerficationToken")
 	public ResponseEntity<ApiResponse> resendToken(@RequestParam(required = true) String email) throws AddressException, MessagingException{
 			User user=this.userRepo.findByEmail(email).orElseThrow(()->new UserNotFound("User","email" , email));
 			if(user.isLocked()) {
 				throw new IllegalStateException("Account Locked Due to Excess Request for Token. Contact Admin to Remove lock Email: "+AppConstants.ADMIN_ACCOUNT_EMAIL);
 			}
 			if(user.isEnabled()) {
 			throw new IllegalStateException("Email Already Verified");
 		}
 			String generatedToken=this.confirmationService.generateSaveToken(email,2);
 			String to=email;
 			String from=AppConstants.ADMIN_ACCOUNT_EMAIL;
 			String text=AppConstants.TEXT+generatedToken;
 			String subject=AppConstants.SUBJECT;
 			this.sender.sendEmail(to, from, subject, text);
 			ApiResponse response=new ApiResponse();
 			response.setMeassage("Confirmation Token Send");
 			response.setSuccess(true);
 			
 			return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);
 			
 		}
 
 	
 	
 	
}
	
	

	

