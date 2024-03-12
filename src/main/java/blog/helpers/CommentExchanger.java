package blog.helpers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import blog.entities.Comment;
import blog.interactors.CommentDto;

//COMMENT EXCHANGER FROM DTO TO ENTITY AND VICE VERSA

public class CommentExchanger {
	
	//reference of necessary class used in this class 
	
	@Autowired
	private ModelMapper mapper;
	
	
	public Comment commentExchanger(CommentDto commentDto) {
		return this.mapper.map(commentDto, Comment.class);
	}
	
	public CommentDto commentDtoExchanger(Comment comment) {
		return this.mapper.map(comment, CommentDto.class);
	}
	
	
	

}
