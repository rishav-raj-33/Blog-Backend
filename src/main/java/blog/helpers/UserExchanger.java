package blog.helpers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import blog.entities.User;
import blog.interactors.UserDto;

//USER EXCHANGER FROM DTO TO ENTITY AND VICE VERSA


public class UserExchanger {
	
	//reference of necessary class used in this class 
	
@Autowired
	private ModelMapper mapper;


public User userExchanger(UserDto user) {
	return this.mapper.map(user, User.class);
}



public UserDto userDtoExchanger(User user) {
	return this.mapper.map(user, UserDto.class);
}


}
