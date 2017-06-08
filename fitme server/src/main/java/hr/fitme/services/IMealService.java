package hr.fitme.services;

import java.util.List;

import hr.fitme.dao.ICommonDao;
import hr.fitme.domain.Meal;
import hr.fitme.domain.User;

public interface IMealService extends ICommonDao<Meal, Integer> {

	List<Meal> getForUser(User user);
	List<Meal> getMealsToday(User user);

}
