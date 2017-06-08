package hr.fitme.services;

import java.util.List;

import hr.fitme.dao.ICommonDao;
import hr.fitme.domain.Food;
import hr.fitme.domain.FoodCategory;

public interface IFoodService extends ICommonDao<Food, Integer>{
	public List<Food> findByCategory(FoodCategory categoryId);
}
