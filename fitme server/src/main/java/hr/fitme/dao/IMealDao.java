package hr.fitme.dao;

import java.util.List;

import hr.fitme.domain.Meal;

public interface IMealDao extends ICommonDao<Meal, Integer> {
	public List<Meal> getMealsToday(int userId);
}
