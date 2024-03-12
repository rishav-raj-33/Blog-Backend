package blog.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import blog.config.AppConstants;
import blog.entities.User;
import blog.helpers.ApiResponse;
import blog.helpers.UserPageResponse;
import blog.interactors.UserDto;
import blog.repositories.UserRepo;
import blog.serviceClass.UserServiceClass;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "UserController",description = "API for User related operations")
@RestController
@RequestMapping("/api/users")
@EnableMethodSecurity(prePostEnabled=true)
public class UserController {
	
	////reference of necessary class used in this class 
	
	@Autowired
	private UserServiceClass userService;
	@Autowired
	private UserRepo repo;
	
	

	
	//CREATE ADMIN API
	
	 @PreAuthorize("hasAnyRole('ADMIN')")	
@PostMapping("/")	
   public ResponseEntity<UserDto> craeteUser( @Valid @RequestBody UserDto userdto) {
	  UserDto savedUser=this.userService.createAdmin(userdto);
    	return  new ResponseEntity<>(savedUser,HttpStatus.CREATED);
	}
	 
	 
	 //GET ALL USER API


	 @PreAuthorize("hasAnyRole('ADMIN')")	 
@GetMapping("/getAll")
public ResponseEntity<UserPageResponse> getUsers(@RequestParam(value="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) int pageNumber,
		@RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)int pageSize,
		@RequestParam(value = "sortBy",defaultValue = AppConstants.USER_SORT_BY,required = false) String sortBy,
		@RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir){
		UserPageResponse response=this.userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);
  return new ResponseEntity<>(response,HttpStatus.OK);
 }
	 
	 //GET USER BY ID API

@GetMapping("/{id}")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public ResponseEntity<UserDto> getUserById(@PathVariable("id") int id){
	      UserDto user=this.userService.getUserById(id);
	return new ResponseEntity<UserDto>(user,HttpStatus.FOUND);	
}

//DELETE USER API

@PreAuthorize("hasAnyRole('ADMIN')")
@DeleteMapping("/{id}")
public ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") int id){
		this.userService.deleteUser(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("user Delted", true),HttpStatus.NO_CONTENT);
}


//UPDATE USER API

@PreAuthorize("hasAnyRole('USER','ADMIN')")
@PutMapping("/{id}")
public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto user,@PathVariable("id") int id){
	 UserDto updatedUser=this.userService.updateUser(user, id);
	return new ResponseEntity<UserDto>(updatedUser,HttpStatus.ACCEPTED);
	
	
}

//API FOR ADMIN TO CLEAN UP DISABLED ACCOUNTS


@DeleteMapping("/remove/disableAccounts")
@PreAuthorize("hasAnyRole('ADMIN')")
public ResponseEntity<ApiResponse> removeDisableAccount(){
	this.userService.disabledUser(false);
	return new ResponseEntity<ApiResponse>(new ApiResponse("Removed Disabled Account", true),HttpStatus.NO_CONTENT);
}


//API FOR GETTING ACTIVE ACCOUNTS
@PreAuthorize("hasAnyRole('ADMIN')")
@GetMapping("/get/activeUsers")
public ResponseEntity<UserPageResponse> getAllActiveUsers(@RequestParam(value="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) int pageNumber,
		@RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)int pageSize,
		@RequestParam(value = "sortBy",defaultValue = AppConstants.USER_SORT_BY,required = false) String sortBy,
		@RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir){
	UserPageResponse listOfUsers=this.userService.getAllActiveUser(pageNumber,pageSize,sortBy,sortDir);
	return new ResponseEntity<>(listOfUsers,HttpStatus.OK);
}

//Change Password By OLd Password
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@PostMapping("/changePassword/")
public ResponseEntity<ApiResponse> changePassword(@RequestParam(required = true) String oldPassword,@RequestParam(required = true) String newPassword,
		@RequestParam(required = true)String email){
	
	this.userService.changePassword(oldPassword, newPassword,email);
	
	return new ResponseEntity<ApiResponse>(new ApiResponse("Password Updated", true),HttpStatus.ACCEPTED);
	
}
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@DeleteMapping("/disableAccount")
public ResponseEntity<ApiResponse> disableAccount(Principal principal){
	
	User currentUser=this.repo.findByEmail(principal.getName()).get();
	this.userService.deleteUser(currentUser.getId());
	ApiResponse response=new ApiResponse();
	response.setMeassage("Account Diabled");
	response.setSuccess(true);
	return new ResponseEntity<ApiResponse>(response,HttpStatus.NO_CONTENT);
	
}




}
