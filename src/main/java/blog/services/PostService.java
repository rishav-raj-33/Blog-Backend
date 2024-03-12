package blog.services;

import blog.helpers.PostPageResponse;
import blog.interactors.PostDto;

//lIST OF METHODS OF POST SERVICE

public interface PostService {
	PostDto createPost(PostDto postDto,Integer userId,Integer categoryId);
	 PostDto updatePost(PostDto postDto,Integer id);
	 void deletePost(Integer id);
	 PostPageResponse getAllPost(int pageNumber,int pageSize,String sortBy,String sortDir);
	 PostDto getPostById(Integer postId);
	 PostPageResponse getPostByCategory(Integer categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);
	 PostPageResponse getPostByUser(Integer userId,int pageNumber,int pageSize,String sortBy,String sortDir);
	 PostPageResponse searchPosts(String keyword,int pageNumber,int pageSize,String sortDir);
	 
	 
	 
	 
	
	
	
	
	
	

}
