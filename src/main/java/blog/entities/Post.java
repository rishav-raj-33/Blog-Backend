package blog.entities;



import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

//POST TABLE IN THE DATABASE

@Entity
@Getter
@Setter
public class Post {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Integer postId;	

private String title;
@Column(length = 1000)
private String content;
private String imageName;
private LocalDate addeDate;





//Mappings

@ManyToOne
@JoinColumn(name = "category_id")
private Category category;
@ManyToOne
private User user;
@OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
private Set<Comment> comments=new HashSet<>();








}

