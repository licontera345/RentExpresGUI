package com.pinguela.rentexpres.model;

public class ProvinceDTO extends ValueObject {

    private Integer id;      
    private String name;     

    public ProvinceDTO() {
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
}
