package blog.services;

import blog.helpers.CommentPageResponse;
import blog.interactors.CommentDto;

//lIST OF METHODS OF COMMENT SERVICE

public interface CommentService {
	
	CommentDto createComment(CommentDto commentDto,Integer postId);
	void deleteComment(Integer commentId);
	
	CommentPageResponse findCommentByPost(int postId,int pageNumber,int pageSize,String sortDir);
	
	
	

}
