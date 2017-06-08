package hr.fitme.dto;


import java.util.Date;

import hr.fitme.domain.Location;

public class LocationDto {
	
	private double lat;
	
	private double lng;
	
	private Date date;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public LocationDto() {
		super();
	}
	
	public LocationDto(Location location) {
		this.lat = location.getLat();
		this.lng = location.getLng();
		this.date = location.getDate();
	}

}
