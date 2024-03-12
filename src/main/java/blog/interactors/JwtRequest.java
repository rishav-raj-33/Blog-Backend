package blog.interactors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//CONATINER TO VERIFIYING USER FROM DATABASE FOR SECURITY(JWT TOKEN)


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtRequest {
	
	private String email;
	private String password;

}
