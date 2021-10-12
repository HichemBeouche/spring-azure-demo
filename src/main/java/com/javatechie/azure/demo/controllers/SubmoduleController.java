package com.javatechie.azure.demo.controllers;

import com.javatechie.azure.demo.models.Submodule;
import com.javatechie.azure.demo.payload.response.MessageResponse;
import com.javatechie.azure.demo.repository.SubmoduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/submodule")
public class SubmoduleController {
	
	@Autowired
	private SubmoduleRepository submoduleRepository;
	
	@GetMapping(path="/{id_submodule}")
	public @ResponseBody Optional<Submodule> getSubmoduleById(@PathVariable Long id_submodule){
		return submoduleRepository.findById(id_submodule);
	}
	
	@GetMapping(path="/{idStory}/story_submodules")
	public @ResponseBody Iterable<Submodule> getSubmodulesStory(@PathVariable Long id_story){
		return submoduleRepository.findAllByIdStory(id_story);
	}
	
	
	
	@PostMapping(path="/{id_module}/createSubmodule")
	public ResponseEntity<?> createSubmodule(@PathVariable Long id_module, @RequestBody Submodule submodule){
		if(id_module == null) {
			return ResponseEntity.badRequest().body(
					new MessageResponse("Error: l'id_module doit être renseigné !"));
		}else if (submodule.getName_submodule() == null || submodule.getName_submodule().isBlank()) {
			return ResponseEntity.badRequest().body(
					new MessageResponse("Error: Le titre ne peut pas être vide!"));
		}else if (submodule.getName_submodule().length() > 40) {
			return ResponseEntity.badRequest().body(
					new MessageResponse("Error: Le titre ne peut pas dépasser 40 caractères!"));
		}
		submoduleRepository.save(submodule);
		return new ResponseEntity<Submodule>(submoduleRepository.save(submodule), HttpStatus.OK);
			
	}
	
	@DeleteMapping(path="/{id_submodule}/delete")
	public ResponseEntity<?> deleteSubmodule(@PathVariable Long id_submodule){
		if(submoduleRepository.findById(id_submodule).isPresent()) {
			submoduleRepository.deleteById(id_submodule);
			return ResponseEntity.ok(new MessageResponse("Submodule supprimé !"));
		}else {
			return ResponseEntity.badRequest().body(
					new MessageResponse("Submodule non supprimé, Submodule introuvable !"));
		}
	}
	
	@DeleteMapping(path="/{id_submodule}/deleteAllByStory")
	public ResponseEntity<?> deleteAllSubmodulesStory(@PathVariable Long id_story){
		if(id_story == null) {
			return ResponseEntity.badRequest().body(
					new MessageResponse("Error: l'id_story doit être renseigné !"));
		}else {
			submoduleRepository.deleteById(id_story);
			return ResponseEntity.ok(new MessageResponse("Tous les submodules de l'histoire renseignée ont été supprimés !"));
		}
	}
	
	@PutMapping(path="/changeName")
	public ResponseEntity<?> changeName (@RequestBody Submodule submodule){
		Submodule res = submoduleRepository.findById(submodule.getId_submodule()).get();
		res.setName_submodule(submodule.getName_submodule());
		submoduleRepository.save(res);
		return ResponseEntity.ok(new MessageResponse("nom du submodule modifié !"));
	}
	
	@PutMapping(path="/changeNote")
	public ResponseEntity<?> changeNote (@RequestBody Submodule submodule){
		Submodule res = submoduleRepository.findById(submodule.getId_submodule()).get();
		res.setNote(submodule.getNote());
		submoduleRepository.save(res);
		return ResponseEntity.ok(new MessageResponse("note modifié !"));
	}
}
