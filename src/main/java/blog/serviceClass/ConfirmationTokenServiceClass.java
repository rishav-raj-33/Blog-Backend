package blog.serviceClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.entities.EmailConfirmationToken;
import blog.entities.User;
import blog.exceptions.UserNotFound;
import blog.repositories.EmailConfirmationTokenRepo;
import blog.repositories.UserRepo;
import blog.services.ConfirmationTokenService;

@Service
public class ConfirmationTokenServiceClass implements ConfirmationTokenService {
	
	@Autowired
	private EmailConfirmationTokenRepo repo;
	@Autowired
	private UserRepo userRepo;
	

	@Override
	public String generateSaveToken(String email,int time) {
		User user=this.userRepo.findByEmail(email).orElseThrow(()->new UserNotFound("User","E-mail" , email));
		List<EmailConfirmationToken> tokens=this.repo.findByUser(user);
		if(tokens.size()>5) {
			user.setLocked(true);
			this.userRepo.save(user);
			return null;
		}
		EmailConfirmationToken token=new EmailConfirmationToken();
		UUID generateToken=UUID.randomUUID();
		token.setToken(generateToken.toString());
		token.setCreateAt(LocalDateTime.now());
		token.setExpires(LocalDateTime.now().plusMinutes(time));
		token.setUser(user);
		this.repo.save(token);
		
		return token.getToken();
	}
	
	//Confirms Token 

	@Override
	public boolean ConfirmToken(String token) {
		EmailConfirmationToken getToken=this.repo.findByToken(token).orElseThrow(()->new IllegalStateException("Confirmation token Not Found"));
		
		if(getToken.getConfirmedAt()!=null) {
			throw new IllegalStateException("E-mail is Already Confirmed");
		}
		
		if(getToken.getExpires().isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("Confirmation Token Expired");
		}
		
		User getUser=this.userRepo.findByEmail(getToken.getUser().getEmail()).get();
		getUser.setEnabled(true);
		this.userRepo.save(getUser);
		deleteAllConfirmationToken(getUser);
		return true;
	}
	
	//Delete All Tokens After Confirmation.

	@Override
	public void deleteAllConfirmationToken(User user) {
		
		List<EmailConfirmationToken> deleteTokens=this.repo.findByUser(user);
		
	  for(EmailConfirmationToken tokenToDelete:deleteTokens) {
		  this.repo.delete(tokenToDelete);
		  }
		
	}
	
	//verify Token

	@Override
	public boolean verifyToken(String token) {
		EmailConfirmationToken getToken=this.repo.findByToken(token).orElseThrow(()->new IllegalStateException("Confirmation token Not Found"));
		if(getToken.getExpires().isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("Confirmation Token Expired");
		}
		User getUser=this.userRepo.findByEmail(getToken.getUser().getEmail()).get();
		deleteAllConfirmationToken(getUser);
		
		return true;
	}
	
	
	

}
