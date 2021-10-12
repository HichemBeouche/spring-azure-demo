package com.javatechie.azure.demo.repository;

import com.javatechie.azure.demo.models.Submodule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmoduleRepository extends JpaRepository<Submodule, Long> {
	Optional<Submodule> findById(Long id_submodule);
	List<Submodule> findAllByIdStory(Long id_story);
	List<Submodule> findAllByIdModule(Integer id_module);
	
}
