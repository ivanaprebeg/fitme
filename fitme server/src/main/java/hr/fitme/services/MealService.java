package hr.fitme.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.fitme.dao.IMealDao;
import hr.fitme.domain.Meal;
import hr.fitme.domain.User;

@Transactional
@Service
public class MealService implements IMealService {

	@Autowired
	IMealDao mealDao;
	
	@Override
	public List<Meal> getAll() {
		return mealDao.getAll();
	}

	@Override
	public Meal getById(Integer id) {
		return mealDao.getById(id);
	}

	@Override
	public Meal add(Meal obj) {
		return mealDao.add(obj);
	}

	@Override
	public void update(Meal obj) {
		mealDao.update(obj);
	}

	@Override
	public void delete(Meal obj) {
		mealDao.delete(obj);
	}

	@Override
	public List<Meal> getForUser(User user) {
		return new ArrayList<Meal>(user.getMeals());
	}
	
	public List<Meal> getMealsToday(User user) {
		return mealDao.getMealsToday(user.getId());
	}
	

}
