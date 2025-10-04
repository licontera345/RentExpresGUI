package com.pinguela.rentexpres.model;

public class UserTypeDTO extends ValueObject {
 

    private Integer id;    
    private String nameTipo; 

    public UserTypeDTO() {
        super();
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameTipo() {
        return nameTipo;
    }
    public void setNameTipo(String nameTipo) {
        this.nameTipo = nameTipo;
    }

    @Override
    public String toString() {
        return nameTipo != null ? nameTipo : "";
    }
}
