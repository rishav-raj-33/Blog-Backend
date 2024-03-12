package blog.helpers;

//CATEGORY RESPONSE PAGE FOR PAGINATION

import java.util.List;

import blog.interactors.CategoryDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryPageResponse {
	private List<CategoryDto> content;
	private int pageNumber;
	private int pageSize;
	private long totalElemnets;
	private int totalPages;
	private boolean isLastPage;

}
