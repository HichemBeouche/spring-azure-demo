package com.javatechie.azure.demo.repository;

import com.javatechie.azure.demo.models.Story;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface StoryRepository extends CrudRepository<Story, Integer> {
	
	@Query("Select s.* from Story s where s.id_story in (select id_story from Story_User where id_user = :idUser and permission='A')")
	List<Story> findAllForUser(@Param("idUser") Long idUser);

	@Query("Select s.* from Story s where s.id_story in (select id_story from Story_User where id_user = :idUser and permission!='A')")
	List<Story> findAllSharedToUser(@Param("idUser") Long id_user);

	@Query("select username from Users, Story_User where Users.id_user = Story_User.id_user and Story_User.permission = 'A' and Story_User.id_story = :idStory;")
	List<String> getUsernameAuthors(@Param("idStory") Long id_story);
}
