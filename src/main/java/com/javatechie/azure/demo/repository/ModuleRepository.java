package com.javatechie.azure.demo.repository;

import com.javatechie.azure.demo.models.Module;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ModuleRepository extends CrudRepository<Module, Integer> {
	@Query("Select m.* from Module m where m.id_parent = :idModule")
	List<Module> findAllChildrenById(@Param("idModule") Integer idModule);

	@Query("Select m.* from Module m where m.id_story = :idStory and m.id_parent is null")
	List<Module> findAllMainModulesForStory(@Param("idStory") Integer idStory);
	
	List<Module>findAllByIdStory(Long id_story);
}
