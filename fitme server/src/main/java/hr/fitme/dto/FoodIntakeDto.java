package hr.fitme.dto;

import hr.fitme.domain.FoodIntake;

public class FoodIntakeDto {

	private int id;

	private FoodDto food;

	private float amount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public FoodDto getFood() {
		return food;
	}

	public void setFood(FoodDto food) {
		this.food = food;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public FoodIntakeDto() {

	}
	
	public FoodIntakeDto(FoodIntake intake) {
		this.id = intake.getId();
		this.food = new FoodDto(intake.getFood());
		this.amount = intake.getAmount();
	}
}
