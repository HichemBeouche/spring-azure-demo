package com.javatechie.azure.demo.controllers;


import com.javatechie.azure.demo.models.Story;
import com.javatechie.azure.demo.models.Module;
import com.javatechie.azure.demo.models.Submodule;
import com.javatechie.azure.demo.models.User;
import com.javatechie.azure.demo.payload.response.MessageResponse;
import com.javatechie.azure.demo.repository.ModuleRepository;
import com.javatechie.azure.demo.repository.StoryRepository;
import com.javatechie.azure.demo.repository.SubmoduleRepository;
import com.javatechie.azure.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="/")
public class StoryController {

	@Autowired
	private StoryRepository storyRepository;

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SubmoduleRepository submoduleRepository;

	//To display all the user's stories
	@GetMapping(path = "stories/{id_user}")
	public @ResponseBody
	Iterable<Story> getAllStories(@PathVariable Long id_user) {
		return storyRepository.findAllForUser(id_user);
	}


	//To display all the username authors
	@GetMapping(path = "usernameAuthors/{id_story}")
	public @ResponseBody
	Iterable<String> getUsernameAuthors(@PathVariable Long id_story) {
		return storyRepository.getUsernameAuthors(id_story);
	}

	//To display all the stories shared to the user
	@GetMapping(path = "sharedstories/{id_user}")
	public @ResponseBody
	Iterable<Story> getAllSharedStories(@PathVariable Long id_user) {
		return storyRepository.findAllSharedToUser(id_user);
	}

	@GetMapping(path = "story/{id_story}/modulesAndSubmodules")
	public @ResponseBody
	Iterable<Module> getAllModuleAndSubmoduleByStory(@PathVariable Long id_story) {
		List<Module> modules = moduleRepository.findAllByIdStory(id_story);
		modules.stream().forEach(module -> {
			List<Submodule> s = submoduleRepository.findAllByIdModule(module.getId());
			module.setTabSubmodule(s);
		});
		return modules;
	}

	@PutMapping(path = "story/saveModulesAndSubmodules")
	public ResponseEntity<?> saveModulesAndSubmodules(@RequestBody List<Module> modules) {
		modules.stream().forEach(module -> {
			Module res = moduleRepository.findById(module.getId()).get();
			res.setChildren(module.getChildren());
			res.setName(module.getName());
			res.setTabSubmodule(module.getTabSubmodule());
			res.getTabSubmodule().stream().forEach(submodule -> {
				Submodule resSub = submoduleRepository.findById(submodule.getId_submodule()).get();
				resSub.setIdModule(submodule.getIdModule());
				resSub.setIdStory(submodule.getIdStory());
				resSub.setName_submodule(submodule.getName_submodule());
				resSub.setNote(submodule.getNote());
				submoduleRepository.save(resSub);
			});
			moduleRepository.save(res);
		});
		return ResponseEntity.ok(new MessageResponse("Modules et submodules sauvegardés !"));

	}

	//To display a story
	@GetMapping(path = "story/{id_story}")
	public @ResponseBody
	Optional<Story> getStoryById(@PathVariable Integer id_story) {
		return storyRepository.findById(id_story);
	}

	//To create a story
	@PostMapping("story/{id_user}/create")
	public ResponseEntity<?> createStory(@PathVariable Long id_user, @RequestBody Story story) {
		try {
			System.out.println(story.getTitle() + " " + story.getTitle() + " " + id_user);
			Story newStory = Story.create(story.getTitle(), story.getDesc(), id_user);
			Story s = storyRepository.save(newStory);
			return new ResponseEntity<Story>(s, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
		}
	}

	//To edit a story
	@PutMapping(path = "story/{id_story}/edit")
	public ResponseEntity<?> editStory(@PathVariable Integer id_story, @RequestBody Story newStory) {
		if (id_story.equals(newStory.getId()) && storyRepository.findById(id_story).isPresent()) {
			try {
				Story updatedStory = storyRepository.findById(id_story).get();
				updatedStory.setTitle(newStory.getTitle());
				updatedStory.setDesc(newStory.getDesc());
				updatedStory.setStory(newStory.getStory());
				storyRepository.save(updatedStory);
				return ResponseEntity.ok(new MessageResponse("L'histoire a été modifiée avec succès !"));
			} catch (IllegalArgumentException e) {
				return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
			}
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: id de l'histoire incohérent ou inexistant !"));
		}
	}

	//To delete a story
	@DeleteMapping("story/{id_story}/delete")
	public ResponseEntity<?> deleteStory(@PathVariable Integer id_story) {
		if (storyRepository.findById(id_story).isPresent()) {
			storyRepository.delete(storyRepository.findById(id_story).get());
			return ResponseEntity.ok(new MessageResponse("L'histoire a été supprimée avec succès !"));
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: id de l'histoire inexistant !"));
		}
	}

	@PostMapping("story/addReaderOrCoAuthor/{id_story}")
	public ResponseEntity<?> addReaderOrCoAuthor(@PathVariable Integer id_story, @RequestBody Map<String, String> json) {
		Boolean userExist = false;
		Optional<User> user = null;
		if (userRepository.existsByUsername(json.get("username"))) {
			user = userRepository.findByUsername(json.get("username"));
			userExist = true;
		} else if (userRepository.existsByEmail(json.get("email"))) {
			user = userRepository.findByEmail(json.get("email"));
			userExist = true;
		}
		if(userExist) {
			Optional<Story> story = storyRepository.findById(id_story);
			if(json.get("role").equals("A")) story.get().addUserPerm(user.get().getId_user(), "A");
			else story.get().addUserPerm(user.get().getId_user(), "R");
			try {
				storyRepository.save(story.get());
				return ResponseEntity.ok(new MessageResponse("User add role successfully!"));
			} catch (Exception e) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: User already has a role"));
			}
		}
		return ResponseEntity
				.badRequest()
				.body(new MessageResponse("error: User cannot be found"));
	}

}
