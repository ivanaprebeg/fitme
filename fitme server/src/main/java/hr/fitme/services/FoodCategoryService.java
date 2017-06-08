package hr.fitme.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.fitme.dao.IFoodCategoryDao;
import hr.fitme.domain.FoodCategory;

@Transactional
@Service
public class FoodCategoryService implements IFoodCategoryService {

	@Autowired
	IFoodCategoryDao foodCategoryDao;
	
	@Override
	public List<FoodCategory> getAll() {
		return foodCategoryDao.getAll();
	}

	@Override
	public FoodCategory getById(Integer id) {
		return foodCategoryDao.getById(id);
	}

	@Override
	public FoodCategory add(FoodCategory obj) {
		return foodCategoryDao.add(obj);
	}

	@Override
	public void update(FoodCategory obj) {
		foodCategoryDao.update(obj);

	}

	@Override
	public void delete(FoodCategory obj) {
		foodCategoryDao.delete(obj);
	}

}
