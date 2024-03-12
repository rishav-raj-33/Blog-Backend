package blog.serviceClass;


import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import blog.interactors.PostDto;
import blog.repositories.CategoryRepo;
import blog.repositories.PostRepo;
import blog.repositories.UserRepo;
import blog.config.AppConstants;
import blog.entities.Category;
import blog.entities.Post;
import blog.entities.User;
import blog.exceptions.ResourceNotFound;
import blog.helpers.PostExchanger;
import blog.helpers.PostPageResponse;
import blog.services.*;

//reference of necessary class used in this class 

@Service
public class PostServiceClass implements PostService {
	@Autowired
	private PostRepo postRepo;
	@Autowired
	private PostExchanger exchanger;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private FileServiceClass fileServiceClass;
	
	@Value("${project.image}")
	private String path;
	
	//CREATE POST SERVICE
	
	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFound("user", "User id",userId));
		Category category=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFound("Category","Category id" , categoryId));
		Post createdPost=this.exchanger.postExchanger(postDto);
		createdPost.setAddeDate(LocalDate.now());
		createdPost.setImageName("default.png");
		createdPost.setCategory(category);
		createdPost.setUser(user);
		this.postRepo.save(createdPost);
		return this.exchanger.postDtoExchanger(createdPost);
	}
	
	
	//UPDATE POST SERVICE
	
	@Override
	public PostDto updatePost(PostDto postDto, Integer id) {
		Post updatePost=this.postRepo.findById(id).orElseThrow(()->new ResourceNotFound("Post","Post Id" , id));
		updatePost.setContent(postDto.getContent());
		updatePost.setTitle(postDto.getTitle());
		this.postRepo.save(updatePost);
		return this.exchanger.postDtoExchanger(updatePost);
	}
	
	//DELETE POST SERVICE
	
	@Override
	public void deletePost(Integer id) {
	Post post=this.postRepo.findById(id).orElseThrow(()->new ResourceNotFound("Post","Post id" , id));
	this.fileServiceClass.deleteFile(post.getImageName(),path);
	this.postRepo.delete(post);
	}
	
	//GET ALL POST SERVICE
	
	@Override
	public PostPageResponse getAllPost(int pageNumber,int pageSize,String sortBy,String sortDir) {
		Sort sort=null;
		if(sortDir.equalsIgnoreCase(AppConstants.SORT_DIR)) {
			sort=Sort.by(sortBy).ascending();
		}else {
			sort=Sort.by(sortBy).descending();
		}
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Post> pagePost=this.postRepo.findAll(pageable);
		List<Post> listOfPosts=pagePost.getContent();
		List<PostDto> listOfPostDtos=listOfPosts.stream().map((obj)->this.exchanger.postDtoExchanger(obj)).toList();
		PostPageResponse response=new PostPageResponse();
		response.setContent(listOfPostDtos);
		response.setPageNumber(pagePost.getNumber());
		response.setPageSize(pagePost.getSize());
		response.setTotalPages(pagePost.getTotalPages());
		response.setTotalElemnets(pagePost.getTotalElements());
		response.setLastPage(pagePost.isLast());
		

		
		return response;
	}
	
	//GET POST BY ID SERVICE
	
	@Override
	public PostDto getPostById(Integer postId) {
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFound("Post","Post id" , postId));
		return this.exchanger.postDtoExchanger(post);
	}
	
	
	//SEARCH POST BY KEYWORD SERVICE
	
	
	@Override
	public PostPageResponse searchPosts(String keyword,int pageNumber,int pageSize,String sortDir) {
		Sort sort=null;
		if(sortDir.equalsIgnoreCase(AppConstants.SORT_DIR)) {
			sort=Sort.by("title").ascending();
		}else {
			sort=Sort.by("title").descending();
		}
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Post> titleContentPost=this.postRepo.findByTitleContaining(keyword,pageable);
		List<Post> listOfPosts=titleContentPost.getContent();
		List<PostDto> listOfPostDtos=listOfPosts.stream().map((obj)->this.exchanger.postDtoExchanger(obj)).toList();
		PostPageResponse response=new PostPageResponse();
		response.setContent(listOfPostDtos);
		response.setLastPage(titleContentPost.isLast());
		response.setPageNumber(titleContentPost.getNumber());
		response.setPageSize(titleContentPost.getSize());
		response.setTotalElemnets(titleContentPost.getTotalElements());
		response.setTotalPages(titleContentPost.getTotalPages());
		
		return response;
	}
	
	//GET POST BY CATEGORY SERVICE
	
	@Override
	public PostPageResponse getPostByCategory(Integer categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {
		Category findCategory=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFound("Category","Category id" , categoryId));
		Sort sort=null;
		if(sortDir.equalsIgnoreCase(AppConstants.SORT_DIR)) {
			sort=Sort.by(sortBy).ascending();
		}else {
			sort=Sort.by(sortBy).descending();
		}
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Post> categoryPost =this.postRepo.findByCategory(findCategory,pageable);
		List<Post> listOfCategoryPosts=categoryPost.getContent();
		List<PostDto> listOfCategoryDtos=listOfCategoryPosts.stream().map((obj)->this.exchanger.postDtoExchanger(obj)).toList();
		PostPageResponse response=new PostPageResponse();
		response.setContent(listOfCategoryDtos);
		response.setLastPage(categoryPost.isLast());
		response.setPageNumber(categoryPost.getNumber());
		response.setPageSize(categoryPost.getSize());
		response.setTotalElemnets(categoryPost.getTotalElements());
		response.setTotalPages(categoryPost.getTotalPages());
		
		return response;
	}
	
	//GET POST BY USER SERVICE
	
	@Override
	public PostPageResponse getPostByUser(Integer userId,int pageNumber,int pageSize,String sortBy,String sortDir) {
		User findUser=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFound("User","User Id", userId));
		Sort sort=null;
		if(sortDir.equalsIgnoreCase(AppConstants.SORT_DIR)) {
			sort=Sort.by(sortBy).ascending();
		}else {
			sort=Sort.by(sortBy).descending();
		}
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Post> userPost=this.postRepo.findByUser(findUser,pageable);
		List<Post> listOfUserPosts=userPost.getContent();
		List<PostDto> listOfUserDtos=listOfUserPosts.stream().map((obj)->this.exchanger.postDtoExchanger(obj)).toList();
		PostPageResponse response=new PostPageResponse();
		response.setContent(listOfUserDtos);
		response.setLastPage(userPost.isLast());
		response.setPageNumber(userPost.getNumber());
		response.setPageSize(userPost.getSize());
		response.setTotalElemnets(userPost.getTotalElements());
		response.setTotalPages(userPost.getTotalPages());
		
		return response;
	}
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
}
