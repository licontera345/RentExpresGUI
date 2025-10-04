package com.pinguela.rentexpres.model;

public class VehicleCriteria {

	private String make;
	private String model;
	private Integer anioDesde;
	private Integer anioHasta;
	private Double precioMax;
	private String licensePlate;
	private String vin;
	private Integer currentMileage;

	// estado vehicles
	private Integer vehicleStatusId;
	private String statusName;

	// category
	private Integer categoryId;
	private String categoryName;

	private Integer pageNumber;
	private Integer pageSize;

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Integer getCurrentMileage() {
		return currentMileage;
	}

	public void setCurrentMileage(Integer currentMileage) {
		this.currentMileage = currentMileage;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public VehicleCriteria() {
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}

	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
	}

	public Double getPrecioMax() {
		return precioMax;
	}

	public void setPrecioMax(Double precioMax) {
		this.precioMax = precioMax;
	}

	public Integer getVehicleStatusId() {
		return vehicleStatusId;
	}

	public void setVehicleStatusId(Integer vehicleStatusId) {
		this.vehicleStatusId = vehicleStatusId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

}
