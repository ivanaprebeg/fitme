package hr.fitme.dto;

import java.sql.Date;

import hr.fitme.domain.Walk;

public class WalkDto {

	private int id;
	
	private float distance;
	
	private Date date;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public WalkDto(){
	}
	
	public WalkDto(Walk walk) {
		this.id = walk.getId();
		this.distance = walk.getDistance();
		this.date = walk.getDate();
	}
	
}
