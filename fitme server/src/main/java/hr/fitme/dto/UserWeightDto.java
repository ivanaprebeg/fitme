package hr.fitme.dto;

import java.util.Date;

import hr.fitme.domain.UserWeight;

public class UserWeightDto {

	private int id;

	private float weight;

	private Date date;

	public UserWeightDto(UserWeight weight) {
		this.id = weight.getId();
		this.weight = weight.getWeight();
		this.date = weight.getDate();
	}
	
	public UserWeightDto() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
