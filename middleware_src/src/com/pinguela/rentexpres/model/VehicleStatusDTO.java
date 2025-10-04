package com.pinguela.rentexpres.model;

public class VehicleStatusDTO extends ValueObject {

	private Integer id;
	private String statusName;

	public VehicleStatusDTO() {
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

	public String toString() {
		return statusName != null ? statusName : "";
	}
}
