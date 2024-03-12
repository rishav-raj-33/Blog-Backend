package blog.interactors;


import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



//POST DATA TRANSFER OBJECT

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
	
	private Integer postId;	
	@NotEmpty(message = "Title Must not be Empty")
	private String title;
	@NotEmpty(message = "Content Must not be Empty")
	private String content;
	private String imageName;
	private LocalDate addeDate;
	private CategoryDto category;
	private UserDto user;
	
	
	
	
	
}
