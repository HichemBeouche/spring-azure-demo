package com.javatechie.azure.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table(value = "Module")
public class Module {
    //Attributes
    @Id
    private Integer idModule;

    @Column(value = "name_module")
    private String name;

    @Column(value = "id_story")
    private Integer idStory;

    @Column(value = "id_parent")
    private Integer idParent;

    @Transient
    private List<Module> children;

    @Transient
    private List<Submodule> TabSubmodule;


    //Constructors
    private Module(Integer idStory, Integer idParent) {
        if (idStory == null) { throw new IllegalArgumentException("Error: L'identifiant de l'histoire ne peut être null !");}
        if (idStory <= 0) { throw new IllegalArgumentException("Error: L'identifiant de l'histoire est incorrect !");}
        if (idParent != null && idParent <= 0) { throw new IllegalArgumentException("Error: L'identifiant de du module parent est incorrect !");}
        this.idStory = idStory;
        this.idParent = idParent;
    }


    //Getters
    public Integer getId() {
        return idModule;
    }
    public Integer getIdModule() {
        return idModule;
    }

    public String getName() { return name; }

    public Integer getIdStory() {
        return idStory;
    }

    public Integer getIdParent() {
        return idParent;
    }

    public List<Module> getChildren() { return children; }

    public List<Submodule> getTabSubmodule() {
		return TabSubmodule;
	}


    //Setters
    public void setName(String newName) {
        if (newName == null || newName.isBlank()) {throw new IllegalArgumentException("Error: Le nom ne peut être vide !");}
        else if (newName.length() > 40) {throw new IllegalArgumentException("Error: Le nom ne peut dépasser 40 caractères !");}
        else { this.name = newName; }
    }
    public void setChildren(List<Module> children) { this.children = children; }

    public void setTabSubmodule(List<Submodule> tabSubmodule) { TabSubmodule = tabSubmodule; }


    //Methods
    public static Module create(String name, Integer idStory, Integer idParent) {
        Module m = new Module(idStory, idParent);
        m.setName(name);
        return m;
    }
}

