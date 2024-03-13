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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import blog.config.AppConstants;
import blog.helpers.ApiResponse;
import blog.helpers.CategoryPageResponse;
import blog.interactors.CategoryDto;
import blog.serviceClass.CategoryServiceClass;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
@Tag(name = "CategoryController",description = "API for CATEGPRY related Operations")
@RestController
@RequestMapping("/api/categories")
@EnableMethodSecurity(prePostEnabled=true)
public class CategoryController {
	
	//reference of necessary class used in this class 
	
	@Autowired
	private CategoryServiceClass categoryService;
	
	
	//Create Category API
	
	@PreAuthorize("hasAnyRole('ADMIN')")	
@PostMapping("/")
public ResponseEntity<CategoryDto> crezteCreateCategory(@Valid @RequestBody CategoryDto category){
	CategoryDto createdCategory=this.categoryService.createCategory(category);
	return new ResponseEntity<>(createdCategory,HttpStatus.CREATED);	
	
}
	
        //Get All Category API
	 @PreAuthorize("hasAnyRole('USER', 'ADMIN')")	 
@GetMapping("/getAll")
public ResponseEntity<CategoryPageResponse> getAllCategory(@RequestParam(value = "pageNumber" ,defaultValue =AppConstants.PAGE_NUMBER,required=false) int pageNumber,
		@RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) int pageSize,
		@RequestParam(value = "SortBy",defaultValue=AppConstants.CATEGORY_SORT_BY,required = false) String sortBy,
		@RequestParam(value="sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir){
	CategoryPageResponse categoryResponse=this.categoryService.getAllCategory(pageNumber,pageSize,sortBy,sortDir);
	return new ResponseEntity<CategoryPageResponse>(categoryResponse,HttpStatus.OK);
			}
	 
	 //GET Category By ID API

	 @PreAuthorize("hasAnyRole('USER', 'ADMIN')") 
@GetMapping("/{id}")
public ResponseEntity<CategoryDto> getCategoryById(@PathVariable int id){
	CategoryDto searchCategoryDTO=this.categoryService.getCategoryById(id);
	return new ResponseEntity<CategoryDto>(searchCategoryDTO,HttpStatus.FOUND);
}
	 
	 //UPDATE Category API
	 
	 @PreAuthorize("hasAnyRole('ADMIN')")
@PutMapping("/{id}")
public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDTO,@PathVariable int id){
	CategoryDto updatedCategoryDTO=this.categoryService.updateCategory(categoryDTO,id);
	return new ResponseEntity<CategoryDto>(updatedCategoryDTO,HttpStatus.ACCEPTED);
}
	 
	 //DELETE Category API

	 @PreAuthorize("hasAnyRole('ADMIN')")
@DeleteMapping("/{id}")
public ResponseEntity<ApiResponse> deleteCategory(@PathVariable int id){
	this.categoryService.deleteCategory(id);
	return new ResponseEntity<ApiResponse>(new ApiResponse("Category Deleted", true),HttpStatus.NO_CONTENT);
}


}
