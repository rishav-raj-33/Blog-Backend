package blog.services;


import blog.helpers.CategoryPageResponse;
import blog.interactors.CategoryDto;

//lIST OF METHODS OF CATEGORY SERVICE

public interface CategoryService {
public	CategoryDto createCategory(CategoryDto category);
public	CategoryDto updateCategory(CategoryDto category,Integer id);
public	CategoryDto getCategoryById(Integer id);
public	CategoryPageResponse getAllCategory(int pageNumber,int pageSize,String sortBy,String sortDir);
public	void deleteCategory(Integer id);

}
