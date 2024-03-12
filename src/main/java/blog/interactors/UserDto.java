package blog.interactors;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//USER DATATRANSFER OBJECT


@NoArgsConstructor
@Setter
@Getter
public class UserDto {
	private Integer id;
	@NotEmpty(message = "Name must Not Be Empty")
	private String name;
	@Email(message = "E-mail is Not Valid")
	private String email;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotEmpty
	@Size(min = 8  ,message = "Password should contain atleast 8 characters")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$",message = "Password format:Must be between 8 and 20 characters long.\r\n"
			+ "Contains at least one digit (0-9).\r\n"
			+ "Contains at least one lowercase letter (a-z).\r\n"
			+ "Contains at least one uppercase letter (A-Z).\r\n"
			+ "Contains at least one special character from the specified list.")
	private String password;
	@NotEmpty(message = "About Must not be Empty")
	private String about;
	
	private Set<RolesDto> roles=new HashSet<>();
	

}
