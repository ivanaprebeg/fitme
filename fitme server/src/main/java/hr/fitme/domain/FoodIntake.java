package hr.fitme.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import hr.fitme.dto.FoodIntakeDto;

@Entity
@Table(name="food_intake")
public class FoodIntake implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="food_id")
	private Food food;
	
	@Column(name="amount")
	private float amount;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
//	@ManyToMany(mappedBy = "intakes")
//	private Set<Meal> meals;

	public FoodIntake(FoodIntakeDto intake, User user) {
		this.amount = intake.getAmount();
		this.food = new Food(intake.getFood());
		this.id = intake.getId();
		this.user = user;
		//this.meals = new HashSet();
		
	}
	
	public FoodIntake(){
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

//	public Set<Meal> getMeals() {
//		return meals;
//	}
//
//	public void setMeals(Set<Meal> meals) {
//		this.meals = meals;
//	}
	
	
}
