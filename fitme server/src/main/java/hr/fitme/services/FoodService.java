package hr.fitme.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.fitme.dao.IFoodDao;
import hr.fitme.domain.Food;
import hr.fitme.domain.FoodCategory;


@Transactional
@Service
public class FoodService implements IFoodService {
	
	@Autowired
	IFoodDao foodDao;

	@Override
	public List<Food> getAll() {
		return foodDao.getAll();
	}

	@Override
	public Food getById(Integer id) {
		return foodDao.getById(id);
	}

	@Override
	public Food add(Food obj) {
		return foodDao.add(obj);
	}

	@Override
	public void update(Food obj) {
		foodDao.update(obj);
		
	}

	@Override
	public void delete(Food obj) {
		foodDao.delete(obj);
		
	}

	@Override
	public List<Food> findByCategory(FoodCategory categoryId) {
		return foodDao.findByCategory(categoryId);
	}

}
