package com.pinguela.rentexpres.model;

public class ReservationStatusDTO extends ValueObject {
	private Integer id;
	private String statusName;

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
		return statusName != null ? statusName : "";
	}

}
