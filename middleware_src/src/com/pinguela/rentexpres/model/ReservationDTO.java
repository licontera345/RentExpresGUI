package com.pinguela.rentexpres.model;

public class ReservationDTO extends ValueObject {

	//reservation
	private Integer id;
	private String startDate;
	private String endDate;

	// user
	private Integer userId;
	private String username;

	// vehicle
	private Integer vehicleId;
	private String make;
	private String licensePlate;
	private String model;
	private Double dailyPrice;

	// customer
	private Integer customerId;
	private String name;
	private String phone;
	private String lastName;

	// estado
	private Integer reservationIdStatus;
	private String statusName;

	private boolean selected;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public ReservationDTO() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Double getDailyPrice() {
		return dailyPrice;
	}

	public void setDailyPrice(Double dailyPrice) {
		this.dailyPrice = dailyPrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getReservationIdStatus() {
		return reservationIdStatus != null ? reservationIdStatus : 1;
	}

	public void setReservationIdStatus(Integer reservationIdStatus) {
		this.reservationIdStatus = reservationIdStatus;
	}

	public void setMatricula(String matricula) {
		this.licensePlate = matricula;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setNameReservationStatus(String statusName) {
		this.statusName = statusName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
	    return statusName;
	}

}
