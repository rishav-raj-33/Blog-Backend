package blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.entities.Roles;


//ROLE REPOSITORY

public interface RoleRepo extends JpaRepository<Roles, Integer> {

}
