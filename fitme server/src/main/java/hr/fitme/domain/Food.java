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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import hr.fitme.dto.FoodDto;


@Entity
@Table(name = "food")
public class Food implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "calories")
	private int calories;
	
	@Column(name = "carbs")
	private int carbs;

	@Column(name = "protein")
	private int protein;
	
	@Column(name = "fat")
	private int fat;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private FoodCategory category;
	
	public Food(FoodDto food) {
		this.calories = food.getCalories();
		this.carbs = food.getCarbs();
		this.category = new FoodCategory(food.getCategoryId());
		this.fat = food.getFat();
		this.id = food.getId();
		this.name = food.getName();
	}
	
	public Food(){
	}
	
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
	public int getCalories() {
		return calories;
	}
	public void setCalories(int calories) {
		this.calories = calories;
	}
	public int getCarbs() {
		return carbs;
	}
	public void setCarbs(int carbs) {
		this.carbs = carbs;
	}
	public int getProtein() {
		return protein;
	}
	public void setProtein(int protein) {
		this.protein = protein;
	}
	public int getFat() {
		return fat;
	}
	public void setFat(int fat) {
		this.fat = fat;
	}
	public FoodCategory getCategory() {
		return category;
	}
	public void setCategory(FoodCategory category) {
		this.category = category;
	}
}
