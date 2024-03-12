package blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import blog.entities.User;
import blog.exceptions.UserNotFound;
import blog.repositories.UserRepo;
@Service
public class UserDetailOverride implements UserDetailsService{
	
	//LOADER CLASS OF THE USER BY USERNAME(E-MAIL)
	
	//reference of necessary class used in this class 	
	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
	User user=this.userRepo.findByEmail(username).orElseThrow(()->new UserNotFound("User", "User Name", username));
		return user;
	}

}
