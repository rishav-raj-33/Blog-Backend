package blog.helpers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import blog.entities.Post;
import blog.interactors.PostDto;

//POST EXCHANGER FROM DTO TO ENTITY AND VICE VERSE

public class PostExchanger {
	
	//reference of necessary class used in this class 
	
	@Autowired
	private ModelMapper mapper;
	
	
	public Post postExchanger(PostDto post) {
		return this.mapper.map(post, Post.class);
	}
	public PostDto postDtoExchanger(Post post) {
		return this.mapper.map(post, PostDto.class);
	}
	
	
	
	
	
	

}
