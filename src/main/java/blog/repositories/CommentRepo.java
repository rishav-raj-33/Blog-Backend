package blog.repositories;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import blog.entities.Comment;
import blog.entities.Post;

//COMMENT REPOSITORY

public interface CommentRepo extends JpaRepository<Comment, Integer> {
	
	Page<Comment> findByPost(Post post,Pageable pageable);

}
