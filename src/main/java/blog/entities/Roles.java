package blog.entities;





import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


//ROLES TABLE IN DATABSE

@Entity
@Setter
@Getter
public class Roles {

	@Id
	private int id;
	
	private String name;
	
	
	
	
}