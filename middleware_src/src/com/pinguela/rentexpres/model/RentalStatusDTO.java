package com.pinguela.rentexpres.model;

public class RentalStatusDTO extends ValueObject {


    private Integer id;       
    private String statusName;  

    public RentalStatusDTO() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    @Override
    public String toString() {
        return statusName;
    }

}
