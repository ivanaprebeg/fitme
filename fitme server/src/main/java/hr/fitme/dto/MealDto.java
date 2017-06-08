package hr.fitme.dto;

import java.util.ArrayList;
import java.util.List;

import hr.fitme.domain.FoodIntake;
import hr.fitme.domain.Meal;

public class MealDto {
	private int id;

	private String name;

	private List<FoodIntakeDto> intakes;
	
	public MealDto(){
		this.intakes = new ArrayList<>();
	}
	
	public MealDto(Meal meal) {
		this.id = meal.getId();
		this.name = meal.getName();
		this.intakes = new ArrayList<>();
		
		for (FoodIntake foodIntake : meal.getIntakes()) {
			this.intakes.add(new FoodIntakeDto(foodIntake));
		}
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

	public List<FoodIntakeDto> getIntakes() {
		return intakes;
	}

	public void setIntakes(List<FoodIntakeDto> intakes) {
		this.intakes = intakes;
	}	

}
