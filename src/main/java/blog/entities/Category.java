package blog.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//CATEGORY TABLE IN THE DATABASE

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Category {
@Id
private Integer categoryId;
private String categoryTitle;
private String categoryDescription;

@OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
private List<Post> listOfPosts=new ArrayList<>();
	
	
}
