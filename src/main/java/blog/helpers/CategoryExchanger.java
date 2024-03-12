package blog.helpers;


//CATEGORY EXCHANGER FROM DTO TO ENTITY AND VICE VERSA


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import blog.entities.Category;
import blog.interactors.CategoryDto;


public class CategoryExchanger {
	
	
	//reference of necessary class used in this class 
	
	@Autowired
	private ModelMapper mapper;


public Category categoryExchanger(CategoryDto category) {
	return this.mapper.map(category, Category.class);
}

public CategoryDto categoryDtoExchanger(Category category) {
	return this.mapper.map(category, CategoryDto.class);
}

}
