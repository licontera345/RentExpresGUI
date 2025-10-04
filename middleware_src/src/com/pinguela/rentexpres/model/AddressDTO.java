package com.pinguela.rentexpres.model;

public class AddressDTO extends ValueObject {


    private Integer id;    
    private Integer idCity;  
    private String street;
    private String streetNumber;

    private String cityName;
    private String provinceName;

    public AddressDTO() {
        super();
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCity() {
        return idCity;
    }
    public void setIdCity(Integer idCity) {
        this.idCity = idCity;
    }

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
