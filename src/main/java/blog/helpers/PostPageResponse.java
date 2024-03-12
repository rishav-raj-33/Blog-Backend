package blog.helpers;

import java.util.List;
import blog.interactors.PostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//POST PAGE RESPONSE FOR PAGINATION

@NoArgsConstructor
@Getter
@Setter
public class PostPageResponse {
	
	private List<PostDto> content;
	private int pageNumber;
	private int pageSize;
	private long totalElemnets;
	private int totalPages;
	private boolean isLastPage;
	
	
}
