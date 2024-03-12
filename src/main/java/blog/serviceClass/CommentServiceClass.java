package blog.serviceClass;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import blog.config.AppConstants;
import blog.entities.Comment;
import blog.entities.Post;
import blog.exceptions.ResourceNotFound;
import blog.helpers.CommentExchanger;
import blog.helpers.CommentPageResponse;
import blog.interactors.CommentDto;
import blog.repositories.CommentRepo;
import blog.repositories.PostRepo;
import blog.services.CommentService;

//COMMENT SERVICE CLASS

@Service
public class CommentServiceClass implements CommentService {
	
	//reference of necessary class used in this class 
	
	@Autowired
	private PostRepo postRepo;
	@Autowired
	private CommentRepo commentRepo;
	@Autowired
	private CommentExchanger exchanger;
	
	
	//CREATE COMMENT SERVICE

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFound("Post","Post Id" , postId));
		Comment comment=this.exchanger.commentExchanger(commentDto);
		comment.setPost(post);
		Comment savedComment=this.commentRepo.save(comment);
		return this.exchanger.commentDtoExchanger(savedComment);
	}
	
	//DELETE COMMENT SERVICE

	@Override
	public void deleteComment(Integer commentId) {
		Comment deleteComment=this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFound("Comment","Comment Id" , commentId));
		this.commentRepo.delete(deleteComment);
	}

	//FIND COMMENT BY POST
	
	@Override
	public CommentPageResponse findCommentByPost(int postId,int pageNumber, int pageSize, String sortDir) {
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFound("Post", "PostId", postId));
		Sort sort=null; 
		if(sortDir.equalsIgnoreCase("asc")) {
			sort=Sort.by(AppConstants.COMMENT_SORT_BY_STRING).ascending();
		}else {
			sort=Sort.by(AppConstants.COMMENT_SORT_BY_STRING).descending();
		}
	Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
	Page<Comment> commentsPage=this.commentRepo.findByPost(post, pageable);
	List<Comment> comments=commentsPage.getContent();
	
	List<CommentDto> listOfCommentDtos=comments.stream().map((obj)->this.exchanger.commentDtoExchanger(obj)).toList();
	CommentPageResponse response=new CommentPageResponse();
	response.setContent(listOfCommentDtos);
	response.setLastPage(commentsPage.isLast());
	response.setPageNumber(commentsPage.getNumber());
	response.setPageSize(commentsPage.getSize());
	response.setTotalElemnets(commentsPage.getTotalElements());
	response.setTotalPages(commentsPage.getTotalPages());
		return response;
	}
	

}
