package hr.fitme.dto;

import java.util.Date;

import hr.fitme.domain.User;

public class UserDto {
	private int id;

	private String username;

	private String email;

	private String password;

	private float height;

	private float desiredWeight;

	private int desiredKcal;

	private Date dateOfBirth;
	
	private Boolean gender;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getDesiredWeight() {
		return desiredWeight;
	}

	public void setDesiredWeight(float desiredWeight) {
		this.desiredWeight = desiredWeight;
	}

	public int getDesiredKcal() {
		return desiredKcal;
	}

	public void setDesiredKcal(int desiredKcal) {
		this.desiredKcal = desiredKcal;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public UserDto(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.height = user.getHeight();
		this.desiredWeight = user.getDesiredWeight();
		this.desiredKcal = user.getDesiredKcal();
		this.dateOfBirth = user.getDateOfBirth();
		this.gender = user.getGender();
		this.email = user.getEmail();
		this.password = user.getPassword();
	}
	
	public UserDto(){
	}
}
