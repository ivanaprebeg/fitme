package hr.fitme.dao;

import java.util.List;

import hr.fitme.domain.Food;
import hr.fitme.domain.FoodCategory;

public interface IFoodDao extends ICommonDao<Food, Integer> {
	public List<Food> findByCategory(FoodCategory category);
}
