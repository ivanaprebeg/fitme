package hr.fitme.dto;

import hr.fitme.domain.FoodCategory;

public class FoodCategoryDto {

	private int categoryId;

	private String name;

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FoodCategoryDto(FoodCategory foodCategory)
	{
		this.setCategoryId(foodCategory.getCategoryId());
		this.setName(foodCategory.getName());
	}
}
