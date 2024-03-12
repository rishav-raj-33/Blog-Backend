package blog.interactors;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//CATEGORY DATA TRANSFER OBJECT

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
	private Integer categoryId;
	@NotEmpty(message = "Title must Not Be Empty")
	private String categoryTitle;
	@NotEmpty(message = "Description must Not Be Empty")
	private String categoryDescription;
}
