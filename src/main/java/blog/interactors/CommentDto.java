package blog.interactors;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

//COMMENT DATA TRANSFER OBJECT

@Getter
@Setter
public class CommentDto {
	
private Integer commentId;
	@NotBlank
	private String content;
	
	

}
