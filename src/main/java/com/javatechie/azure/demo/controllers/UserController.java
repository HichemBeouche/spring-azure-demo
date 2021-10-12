package com.javatechie.azure.demo.controllers;

import com.javatechie.azure.demo.models.User;
import com.javatechie.azure.demo.payload.response.MessageResponse;
import com.javatechie.azure.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {

	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(path="/{id_user}")
	public @ResponseBody Optional<User> getUserById(@PathVariable Long id_user){
		return userRepository.findById(id_user);
	}
	
	@GetMapping(path="/users")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@PutMapping(path="/changePassword")
	public ResponseEntity<?> changePassword (@RequestBody User user) {
		User res = userRepository.findById(user.getId_user()).get();
		res.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(res);
		return ResponseEntity.ok(new MessageResponse("Password successfully changed !"));
		
	}
	
	@PutMapping(path="/changeUsername")
	public ResponseEntity<?> changeUsername (@RequestBody User user) {
		if (userRepository.existsByUsername(user.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}
		
		User res = userRepository.findById(user.getId_user()).get();
		res.setUsername(user.getUsername());
		userRepository.save(res);
		return ResponseEntity.ok(new MessageResponse("Username successfully changed !"));
	}
	
	@PutMapping(path="/changeMail")
	public ResponseEntity<?> changeMail (@RequestBody User user){
		if (userRepository.existsByEmail(user.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		User res = userRepository.findById(user.getId_user()).get();
		res.setEmail(user.getEmail());
		userRepository.save(res);
		return ResponseEntity.ok(new MessageResponse("Email successfully changed !"));
	}
		
	
	
}
