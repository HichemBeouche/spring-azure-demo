package com.javatechie.azure.demo.security.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserIdDetailsServiceImpl {
    UserDetails loadUserByUserId(Long id);
}
