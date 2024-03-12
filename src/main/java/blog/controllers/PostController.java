package blog.controllers;


import java.io.IOException;
import java.io.InputStream;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import blog.config.AppConstants;
import blog.helpers.ApiResponse;
import blog.helpers.PostPageResponse;
import blog.interactors.PostDto;
import blog.serviceClass.PostServiceClass;
import blog.services.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Tag(name = "PostController",description = "API for Post related operations")
@RestController
@RequestMapping("/api/")
public class PostController {
	
	//reference of necessary class used in this class 
	
	@Autowired
	private PostServiceClass postService;
	@Autowired
	private FileService fileService;
	
	//variable of path for image upload used by this class 
	
	@Value("${project.image}")
	private String path;
	
	
	//CREATE POST API
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto post,@PathVariable int userId,@PathVariable int categoryId ){
		PostDto createdPost=this.postService.createPost(post, userId, categoryId);
		return new ResponseEntity<PostDto>(createdPost,HttpStatus.CREATED);
	}
	
	//GET POST BY USER API
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<PostPageResponse> getPostByUser(@PathVariable int userId,@RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)int pageNumber,
			@RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)int pageSize,
			@RequestParam(value = "sortBy",defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
			@RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir){
		PostPageResponse response=this.postService.getPostByUser(userId,pageNumber,pageSize,sortBy,sortDir);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	//GET POST BY CATEGORY API
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<PostPageResponse> getPostByCategory(@PathVariable int categoryId ,@RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) int PageNumber,
			@RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) int pageSize,
			@RequestParam(value = "sortBy",defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
			@RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir){
   PostPageResponse getPostByCategory=this.postService.getPostByCategory(categoryId,PageNumber,pageSize,sortBy,sortDir);
   return new ResponseEntity<>(getPostByCategory,HttpStatus.OK);
	}
	
	//SHOW ALL POST API
	
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping("/posts/getAll")
	public ResponseEntity<PostPageResponse> showAllPosts(@RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) int pageNumber
			,@RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) int pageSize,
			@RequestParam(value = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
			@RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = true) String sortDir){
		PostPageResponse postResponse=this.postService.getAllPost(pageNumber,pageSize,sortBy,sortDir);
		return new ResponseEntity<PostPageResponse>(postResponse,HttpStatus.OK);
	}
	
	
	//Get POST BY ID API
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping("/posts/{id}")
	public ResponseEntity<PostDto> getPostById(@PathVariable int id){
		PostDto postDto=this.postService.getPostById(id);
		return new ResponseEntity<PostDto>(postDto,HttpStatus.FOUND);
	}
	
	//DELETE POST API
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@DeleteMapping("/posts/{id}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable int id){
		this.postService.deletePost(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post deleted", true),HttpStatus.NO_CONTENT);
	}
	
	//UPDATE POST API
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@PutMapping("/posts/{id}")
	public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto post,@PathVariable int id){
		PostDto postDto=this.postService.updatePost(post, id);
		return new ResponseEntity<PostDto>(postDto,HttpStatus.ACCEPTED);
	}
	
	//GET POST BY KEYWORDS
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping("/posts/search/{keyword}")
	public ResponseEntity<PostPageResponse> getSearchedPostByTitle(@PathVariable String keyword,
			@RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)int pageNumber,
			@RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)int pageSize,
			@RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir){
		PostPageResponse response=this.postService.searchPosts(keyword,pageNumber,pageSize,sortDir);
		return new ResponseEntity<PostPageResponse>(response,HttpStatus.OK);
	}
	
	//UPLOAD IMAGE OF POST API
	
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@PostMapping("/posts/image/upload/{postId}")
	public ResponseEntity<ApiResponse> uploadPostImage(@RequestParam MultipartFile image,@PathVariable int postId) throws IOException{
		PostDto getPost=this.postService.getPostById(postId);
		String fileName=this.fileService.uploadImage(path, image);
		getPost.setImageName(fileName);
		this.postService.updatePost(getPost, postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("File Name: "+image.getOriginalFilename(), true),HttpStatus.ACCEPTED);
	}
	
	
	//SERVING IMAGE OF POST API
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping(value = "posts/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
	void downloadImage(@PathVariable String imageName,HttpServletResponse response) throws IOException {
        InputStream resource=this.fileService.getResource(path, imageName);		
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
 
	

}
;