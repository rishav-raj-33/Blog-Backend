package blog.services;



import blog.entities.User;


//Confirmation Token Service
public interface ConfirmationTokenService {
	
	  public String generateSaveToken(String email,int time);
      public boolean ConfirmToken(String token);
      public void deleteAllConfirmationToken(User user);
      
      public boolean verifyToken(String token);
      

}
