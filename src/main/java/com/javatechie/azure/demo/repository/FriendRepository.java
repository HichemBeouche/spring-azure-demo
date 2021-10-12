package com.javatechie.azure.demo.repository;

import com.javatechie.azure.demo.models.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {

}
