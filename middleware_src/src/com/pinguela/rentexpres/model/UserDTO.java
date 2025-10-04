package com.pinguela.rentexpres.model;

import java.io.File;
import java.util.List;

public class UserDTO extends ValueObject {

    private Integer id;
    private String name;
    private String lastName;
    private String secondLastName;
    private String email;
    private String phone;
    private String contrasena;
    private String username;
    private Integer userIdType;
    private String usernameType;
    
    
    private List<File> imagenes;

    public UserDTO() {
        super();
    }

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getSecondLastName() {
        return secondLastName;
    }
    
    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Integer getUserIdType() {
        return userIdType;
    }
    
    public void setUserIdType(Integer userIdType) {
        this.userIdType = userIdType;
    }
    
    public String getUsernameType() {
        return usernameType;
    }
    
    public void setUsernameType(String usernameType) {
        this.usernameType = usernameType;
    }

    public List<File> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<File> imagenes) {
        this.imagenes = imagenes;
    }
}
