package hr.fitme.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import hr.fitme.dto.MealDto;

@SuppressWarnings("serial")
@Entity
@Table(name="meal")
public class Meal implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(name = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(name = "meal_food", joinColumns = {
			@JoinColumn(name = "meal_id") },
			inverseJoinColumns = { @JoinColumn(name = "food_intake_id")})
	private Set<FoodIntake> intakes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<FoodIntake> getIntakes() {
		return intakes;
	}

	public void setIntakes(Set<FoodIntake> intakes) {
		this.intakes = intakes;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Meal() {
		
	}
	
	public Meal(MealDto meal, User user) {
		this.name = meal.getName();
		this.user = user;
		this.date = new Date(Calendar.getInstance().getTime().getTime());
	}
	

}
