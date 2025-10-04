package com.pinguela.rentexpres.model;

public class VehicleCategoryDTO extends ValueObject {


    private Integer id;          
    private String categoryName; 

    public VehicleCategoryDTO() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String toString() {
        return categoryName != null ? categoryName : "";
    }
}
