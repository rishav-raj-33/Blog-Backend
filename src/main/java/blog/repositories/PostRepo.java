package blog.repositories;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import blog.entities.Category;
import blog.entities.Post;
import blog.entities.User;

//POST REPOSITORY


public interface PostRepo extends JpaRepository<Post, Integer> {
	Page<Post> findByUser(User user,Pageable pageable);
	Page<Post> findByCategory(Category category,Pageable pageable);
	Page<Post> findByTitleContaining(String title,Pageable pageable);
}
