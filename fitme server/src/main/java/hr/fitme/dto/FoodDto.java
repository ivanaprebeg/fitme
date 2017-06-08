package hr.fitme.dto;

import hr.fitme.domain.Food;
import hr.fitme.domain.FoodCategory;

public class FoodDto {

	private int id;

	private String name;

	private int calories;

	private int carbs;

	private int protein;

	private int fat;
	
	private int categoryId;

	public FoodDto(Food food) {
		this.calories = food.getCalories();
		this.carbs = food.getCarbs();
		this.fat = food.getFat();
		this.id = food.getId();
		this.name = food.getName();
		this.protein = food.getProtein();
		this.categoryId = food.getCategory().getCategoryId();
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

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(FoodCategory category) {
		this.categoryId = category.getCategoryId();
	}
	
	public FoodDto(){
	}
	
	

}
