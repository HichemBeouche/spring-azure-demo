package com.javatechie.azure.demo.models;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("Story_User")
public class UserId {
    @Column(value = "id_user")
    private Long idUser;

    @Column(value = "permission")
    private String permission;

    //Constructor
    public UserId(Long idUser, String permission) {
        this.idUser = idUser;
        this.permission = permission;
    }

    //Getters
    public Long getIdUser() {
        return idUser;
    }

    public String getPermission() {
        return permission;
    }


    //Setters
    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public void setPermission(String permission) { this.permission = permission; }
}
