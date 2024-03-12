package blog.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import blog.config.AppConstants;
import blog.helpers.ApiResponse;
import blog.helpers.CommentPageResponse;
import blog.interactors.CommentDto;
import blog.serviceClass.CommentServiceClass;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/")
@EnableMethodSecurity(prePostEnabled=true)
@Tag(name = "CommenTController",description = "API for Comment related operations")
public class CommentController {
	
	//reference of necessary class used in this class 
	
	@Autowired
	private CommentServiceClass serviceClass;
	
	//Create Comment API
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@PostMapping("/post/{postId}/comments")
	public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto comment,@PathVariable int postId){
		
		CommentDto createdComment=this.serviceClass.createComment(comment, postId);
		return new ResponseEntity<CommentDto>(createdComment,HttpStatus.CREATED);
	}
	//Delete Comment API
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable int commentId){
		this.serviceClass.deleteComment(commentId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment Deleted", true),HttpStatus.NO_CONTENT);
	}
	
	//GET comment By Post
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping("/post/{postId}/comments")
	public ResponseEntity<CommentPageResponse> getCommentByPost(@PathVariable int postId,@RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)int pageNumber,
			@RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)int pageSize,
			@RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir){
		CommentPageResponse response=this.serviceClass.findCommentByPost(postId, pageNumber, pageSize, sortDir);
		
		return new ResponseEntity<CommentPageResponse>(response,HttpStatus.OK);
		
		
	}
		

	
	
}
