package blog.helpers;

import java.util.List;

import blog.interactors.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//USER PAGE RESPONSE FOR PAGINATION

@Getter
@Setter
@NoArgsConstructor
public class UserPageResponse {
	
	private List<UserDto> content;
	private int pageNumber;
	private int pageSize;
	private long totalElemnets;
	private int totalPages;
	private boolean isLastPage;

}
