package hr.fitme.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import hr.fitme.dto.UserDto;

@Entity
@Table(name = "user")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "height")
	private float height;
	
	@Column(name = "desired_weight")
	private float desiredWeight;
	
	@Column(name = "desired_kcal")
	private int desiredKcal;
	
	@Column(name = "date_of_birth")
	private Date dateOfBirth;
	
	@Column(name = "gender")
	private Boolean gender;
	
	@OneToMany(mappedBy = "user")
	private Set<UserWeight> userWeight;
	
	@OneToMany(mappedBy = "user")
	private Set<FoodIntake> intakes;

	@OneToMany(mappedBy = "user")
	private Set<Meal> meals;
	
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
	public Set<UserWeight> getUserWeight() {
		return userWeight;
	}
	public void setUserWeight(Set<UserWeight> userWeight) {
		this.userWeight = userWeight;
	}
	public Set<FoodIntake> getIntakes() {
		return intakes;
	}
	public void setIntakes(Set<FoodIntake> intakes) {
		this.intakes = intakes;
	}
	public Set<Meal> getMeals() {
		return meals;
	}
	public void setMeals(Set<Meal> meals) {
		this.meals = meals;
	}
	
	public Boolean getGender() {
		return gender;
	}
	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public User(UserDto userDto) {
		super();
		this.id = userDto.getId();
		this.username = userDto.getUsername();
		this.email = userDto.getEmail();
		this.password = userDto.getPassword();
		this.height = userDto.getHeight();
		this.desiredWeight = userDto.getDesiredWeight();
		this.desiredKcal = userDto.getDesiredKcal();
		this.dateOfBirth = userDto.getDateOfBirth();
		this.gender = userDto.getGender();
		this.userWeight = null;
		this.intakes = null;
		this.meals = null;
	}
	
	public User(){
	}
	
	
}
