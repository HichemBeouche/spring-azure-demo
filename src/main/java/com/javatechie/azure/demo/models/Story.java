package com.javatechie.azure.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Table(value = "Story")
public class Story {
    //Attributes
    @Id
    private Integer idStory;

    @Column(value = "title_story")
    private String title;

    @Column(value = "desc_story")
    private String desc;

    @Column(value = "story_story")
    private String story;

    @MappedCollection(idColumn = "id_story")
    private Set<UserId> users = new HashSet<>();


    //Constructor
    private Story() {}


	//Getters
    public Integer getId() {
        return idStory;
    }

    public Integer getIdStory() {
        return idStory;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getStory() {
        return story;
    }
	
	public Set<Long> getUserIds() {
        return this.users.stream()
                .map(UserId::getIdUser)
                .collect(Collectors.toSet());
    }


	//Setters
    public void setTitle(String newTitle) {
        if (newTitle == null || newTitle.isBlank()) {throw new IllegalArgumentException("Error: Le titre ne peut être vide !");}
        else if (newTitle.length() > 40) {throw new IllegalArgumentException("Error: Le titre ne peut dépasser 40 caractères !");}
        else { this.title = newTitle; }
    }

    public void setDesc(String newDesc) {
        if (newDesc != null && newDesc.length() > 200) {throw new IllegalArgumentException("Error: La description ne peut dépasser 200 caractères !");}
        else { this.desc = newDesc; }
    }
	
	public void setStory(String newStory) {
        //Condition à modifier une fois le stockage de l'histoire discuté
        if (newStory != null && Long.valueOf(newStory.length()) > Long.parseLong("4294967295")) {throw new IllegalArgumentException("Error: L'histoire ne peut dépasser 4 294 967 295 caractères !");}
        else { this.story = newStory; }
    }

    /*
    public void setUsersIds(Set<UserId> users) {
        this.users = users;
    }*/
	
	
	//Methods
    public static Story create(String title, String desc, long idAuthor) {
        if (idAuthor <= 0) { throw new IllegalArgumentException("Error: L'identifiant de l'auteur est incorrect !");}
        Story s = new Story();
        s.setTitle(title);
        s.setDesc(desc);
        s.addUserPerm(idAuthor, "A");
        return s;
    }

    public void addUserPerm(long idUser, String perm) {
        if (idUser <= 0) { throw new IllegalArgumentException("Error: L'identifiant de l'utilisateur est incorrect !"); }
        //Condition à modifier une fois les différentes permissions discutées
        else if (perm == null || perm.length() > 2) { throw new IllegalArgumentException("Error: La permission renseignée n'est pas disponible !"); }
        else { this.users.add(new UserId(idUser, perm)); }
    }

    @Override
    public String toString() {
        return "Story{" +
                "idStory=" + idStory +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", story='" + story + '\'' +
                ", users=" + users +
                '}';
    }
}
