package blog.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import blog.entities.Category;


//CATEGORY REPOSITORY

public interface CategoryRepo extends JpaRepository<Category, Integer> {
 

}
