package blog.repositories;





import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import blog.entities.User;

//USER REPOSITORY

public interface UserRepo extends JpaRepository<User, Integer>{
	
	Optional<User> findByEmail(String email);
	
	//Find  Active Users 
	
	Page<User> findByEnabled(boolean flag,Pageable pageable);  
	
	//Find Disable users
	
	List<User> findByEnabled(boolean flag);
	
	
	
	

}
