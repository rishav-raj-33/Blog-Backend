package blog.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.entities.EmailConfirmationToken;
import blog.entities.User;

//Email Confirmation Token Repository

public interface EmailConfirmationTokenRepo extends JpaRepository<EmailConfirmationToken, Integer> {
	
	Optional<EmailConfirmationToken> findByToken(String token);
	
	List<EmailConfirmationToken> findByUser(User user);
}
