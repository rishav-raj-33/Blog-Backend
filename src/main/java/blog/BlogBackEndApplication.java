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
import blog.entities.Category;
import blog.entities.Roles;
import blog.entities.User;
import blog.helpers.CategoryExchanger;
import blog.helpers.CommentExchanger;
import blog.helpers.EmailSender;
import blog.helpers.PostExchanger;
import blog.helpers.UserExchanger;
import blog.repositories.CategoryRepo;
import blog.repositories.RoleRepo;
import blog.repositories.UserRepo;


@SpringBootApplication
public class BlogBackEndApplication implements CommandLineRunner {
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private UserRepo repo;

	@Autowired
	private CategoryRepo categoryRepo;

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
				 
				//Insert Category
				
				Category category=new Category();
				category.setCategoryId(AppConstants.CATEGORY_ID1);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION1);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE1);
				categoryRepo.save(category);  // 1
				
				category.setCategoryId(AppConstants.CATEGORY_ID2);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION2);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE2);
				categoryRepo.save(category);  // 2
				

				category.setCategoryId(AppConstants.CATEGORY_ID3);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION3);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE3);
				categoryRepo.save(category);  // 3

				category.setCategoryId(AppConstants.CATEGORY_ID4);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION4);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE4);
				categoryRepo.save(category);  // 4
				

				category.setCategoryId(AppConstants.CATEGORY_ID5);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION5);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE5);
				categoryRepo.save(category);  // 5
				

				category.setCategoryId(AppConstants.CATEGORY_ID6);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION6);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE6);
				categoryRepo.save(category);  // 6
				

				category.setCategoryId(AppConstants.CATEGORY_ID7);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION7);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE7);
				categoryRepo.save(category);  // 7
				

				category.setCategoryId(AppConstants.CATEGORY_ID8);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION8);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE8);
				categoryRepo.save(category);  // 8
				

				category.setCategoryId(AppConstants.CATEGORY_ID9);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION9);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE9);
				categoryRepo.save(category);  // 9
				

				category.setCategoryId(AppConstants.CATEGORY_ID10);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION10);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE10);
				categoryRepo.save(category);  // 10
				

				category.setCategoryId(AppConstants.CATEGORY_ID11);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION11);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE11);
				categoryRepo.save(category);  // 11
				

				category.setCategoryId(AppConstants.CATEGORY_ID12);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION12);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE12);
				categoryRepo.save(category);  // 12
				

				category.setCategoryId(AppConstants.CATEGORY_ID13);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION13);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE13);
				categoryRepo.save(category);  // 13
				

				category.setCategoryId(AppConstants.CATEGORY_ID14);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION14);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE14);
				categoryRepo.save(category);  // 14
				

				category.setCategoryId(AppConstants.CATEGORY_ID15);
				category.setCategoryDescription(AppConstants.CATEGORY_DESCRIPTION15);
				category.setCategoryTitle(AppConstants.CATEGORY_TITLE15);
				categoryRepo.save(category);  // 15

				
				
				
				
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
    
    

}
