package com.javatechie.azure.demo.repository;

import com.javatechie.azure.demo.models.ERole;
import com.javatechie.azure.demo.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
