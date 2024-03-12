package blog.helpers;


import java.util.List;
import blog.interactors.CommentDto;
import lombok.Getter;
import lombok.Setter;

//COMMENT PAGE RESPONSE FOR PAGINATION

@Getter
@Setter
public class CommentPageResponse {
	
	private List<CommentDto> content;
	private int pageNumber;
	private int pageSize;
	private long totalElemnets;
	private int totalPages;
	private boolean isLastPage;

}
