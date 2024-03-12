package blog;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import blog.config.AppConstants;
import blog.entities.Roles;
import blog.entities.User;
import blog.helpers.CategoryExchanger;
import blog.helpers.CommentExchanger;
import blog.helpers.EmailSender;
import blog.helpers.PostExchanger;
import blog.helpers.UserExchanger;
import blog.repositories.RoleRepo;
import blog.repositories.UserRepo;


@SpringBootApplication
public class BlogBackEndApplication implements CommandLineRunner {
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private UserRepo repo;

	public static void main(String[] args) {
		SpringApplication.run(BlogBackEndApplication.class, args);
		
	}
	
	//Beans for necessary classes for the Application

    @Bean
    ModelMapper createModelMapper() {
		return new ModelMapper();
	}
    
    
    @Bean
    CategoryExchanger creteCategoryExchanger() {
    	return new CategoryExchanger();
    }
    
    @Bean
    UserExchanger createUserExchanger() {
    	return new UserExchanger();
    }
    @Bean
    PostExchanger createPostExchanger() {
    	return new PostExchanger();
    }
    @Bean
    CommentExchanger createCommentExchanger() {
    	return new CommentExchanger();
    }
    
    @Bean
     AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
    
    @Bean
     PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    EmailSender emailSender() {
    	return new EmailSender();
    }
    
    
    //Insert the Default Values whenever Application runs for the first Time.

	@Override
	public void run(String... args) throws Exception {
		
		try {
			   //Insert Roles.
			
			
			Roles roles=new Roles();
			roles.setId(AppConstants.USER);
			roles.setName("ROLE_USER");
			Roles roles2=new Roles();
			roles2.setId(AppConstants.ADMIN);
			roles2.setName("ROLE_ADMIN");
			List<Roles>listOfRoles=List.of(roles,roles2);
				this.roleRepo.saveAll(listOfRoles);
				
				
				 //Insert ADmin Account.
				
				
				Optional<User> getUserByEmail=this.repo.findByEmail(AppConstants.ADMIN_ACCOUNT_EMAIL);
				
				if(!getUserByEmail.isPresent()) {
					User user=new User();
					user.setId(0);
					user.setEmail(AppConstants.ADMIN_ACCOUNT_EMAIL);
					user.setEnabled(true);                            
		           user.setName(AppConstants.ADMIN_ACCOUNT_NAME);
		           user.setAbout(AppConstants.ADMIN_ACCOUNT_ABOUT);
		           user.setPassword(AppConstants.B_CRYPT_PASSWORD);  
		           Roles role=this.roleRepo.findById(AppConstants.ADMIN).get(); 
		   		   user.getRoles().add(role);
		           this.repo.save(user);
				}
				 
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
    
    

}
