package com.javatechie.azure.demo.controllers;

import java.util.Optional;
import com.javatechie.azure.demo.models.Module;
import com.javatechie.azure.demo.payload.response.MessageResponse;
import com.javatechie.azure.demo.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="/")
public class ModuleController {
    @Autowired
    private ModuleRepository moduleRepository;

    //To display all the story's main modules
    @GetMapping(path="modules/{id_story}")
    public @ResponseBody Iterable<Module> getAllMainModules(@PathVariable Integer id_story) {
        return moduleRepository.findAllMainModulesForStory(id_story);
    }

    //To display a module
    @GetMapping(path="module/{id_module}")
    public @ResponseBody Optional<Module> getModuleById(@PathVariable Integer id_module) {
        if (moduleRepository.findById(id_module).isPresent()) {
            Module module = moduleRepository.findById(id_module).get();
            module.setChildren(moduleRepository.findAllChildrenById(id_module));
            moduleRepository.save(module);
        }
        return moduleRepository.findById(id_module);
    }

    //To create a module
    @PostMapping("module/{id_story}/create")
    public ResponseEntity<?> createModule(@PathVariable Integer id_story, @RequestBody Module module) {
        try {
            Module newModule = Module.create(module.getName(), id_story, module.getIdParent());
            Module m = moduleRepository.save(newModule);
            return new ResponseEntity<Module>(m, HttpStatus.OK);
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    //To edit a module
    @PutMapping(path="module/{id_module}/edit")
    public ResponseEntity<?> editModule (@PathVariable Integer id_module, @RequestBody Module newModule) {
        if (id_module.equals(id_module) && moduleRepository.findById(id_module).isPresent()) {
            Module updatedModule = moduleRepository.findById(id_module).get();
            updatedModule.setName(newModule.getName());
            moduleRepository.save(updatedModule);
            return ResponseEntity.ok(new MessageResponse("Le module a été modifié avec succès !"));
        }
        else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: id du module incohérent ou inexistant !"));
        }
    }

    //To delete a module
    @DeleteMapping("module/{id_module}/delete")
    public ResponseEntity<?> deleteModule(@PathVariable Integer id_module) {
        if (moduleRepository.findById(id_module).isPresent()) {
            moduleRepository.delete(moduleRepository.findById(id_module).get());
            return ResponseEntity.ok(new MessageResponse("Le module a été supprimé avec succès !"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: id du module inexistant !"));
        }
    }
}
