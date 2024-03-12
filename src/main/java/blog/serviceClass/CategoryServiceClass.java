package blog.serviceClass;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import blog.config.AppConstants;
import blog.entities.Category;
import blog.entities.Post;
import blog.exceptions.ResourceNotFound;
import blog.helpers.CategoryExchanger;
import blog.helpers.CategoryPageResponse;
import blog.interactors.CategoryDto;
import blog.repositories.CategoryRepo;
import blog.services.CategoryService;


//CATEGORY SERVICE CLASS

@Service
public class CategoryServiceClass implements CategoryService {
	
	//reference of necessary class used in this class 
	
	@Autowired
	private CategoryRepo categoryRepository;
	@Autowired
	private CategoryExchanger exchanger;
	
	@Autowired
	private FileServiceClass fileServiceClass;
	
	@Value("${project.image}")
	String path;

	
	//CREATE CATEGORY SERVICE
	
	@Override
	public CategoryDto createCategory(CategoryDto category) {
	Category createdCategory=this.categoryRepository.save(this.exchanger.categoryExchanger(category));
		return this.exchanger.categoryDtoExchanger(createdCategory);
	}
	
	//UPDATE CATEGORY SERVICE

	@Override
	public CategoryDto updateCategory(CategoryDto category, Integer id) {
		Category getCategory=this.categoryRepository.findById(id).orElseThrow(()->new ResourceNotFound("Category","id" , id));
          getCategory.setCategoryDescription(category.getCategoryDescription());
          getCategory.setCategoryTitle(category.getCategoryTitle());
          this.categoryRepository.save(getCategory);
          return this.exchanger.categoryDtoExchanger(getCategory);
	}
	
	//GET CATEGORY BY ID SERVICE

	@Override
	public CategoryDto getCategoryById(Integer id) {
		Category findCategory=this.categoryRepository.findById(id).orElseThrow(()->new ResourceNotFound("Category","id" , id));
		return this.exchanger.categoryDtoExchanger(findCategory);
	}
	
	
	//GET ALL CATEGORY SERVICE

	@Override
	public CategoryPageResponse getAllCategory(int pageNumber,int pageSize,String sortBy,String sortDir) {
		Sort sort=null;
		if(sortDir.equalsIgnoreCase(AppConstants.SORT_DIR)) {
			sort=Sort.by(sortBy).ascending();
		} else {
			sort=Sort.by(sortBy).descending();
		}
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
	Page<Category> categoryPage=this.categoryRepository.findAll(pageable);
	List<Category> listOfCategories=categoryPage.getContent();
	List<CategoryDto> listOfCategoryDTOs=listOfCategories.stream().map((obj)->this.exchanger.categoryDtoExchanger(obj)).toList();
	CategoryPageResponse response=new CategoryPageResponse();
	response.setContent(listOfCategoryDTOs);
	response.setLastPage(categoryPage.isLast());
	response.setPageNumber(categoryPage.getNumber());
	response.setPageSize(categoryPage.getSize());
	response.setTotalPages(categoryPage.getTotalPages());
	response.setTotalElemnets(categoryPage.getTotalElements());
		return response;
	}
	
	//DELETE CATEGORY SERVICE

	@Override
	public void deleteCategory(Integer id) {
		Category deleteCategory=this.categoryRepository.findById(id).orElseThrow(()->new ResourceNotFound("Category","id" , id));
		List<Post> listOfPosts=deleteCategory.getListOfPosts();
		for(Post post:listOfPosts) {
			this.fileServiceClass.deleteFile(post.getImageName(), path);
		}
		this.categoryRepository.delete(deleteCategory);
	}
	
	
	
	
	
	
	

}
