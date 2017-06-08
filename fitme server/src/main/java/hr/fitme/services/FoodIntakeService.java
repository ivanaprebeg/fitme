package hr.fitme.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.fitme.dao.IFoodIntakeDao;
import hr.fitme.domain.FoodIntake;

@Transactional
@Service
public class FoodIntakeService implements IFoodIntakeService {

	@Autowired
	IFoodIntakeDao foodIntakeDao;
	
	@Override
	public List<FoodIntake> getAll() {
		return foodIntakeDao.getAll();
	}

	@Override
	public FoodIntake getById(Integer id) {
		return foodIntakeDao.getById(id);
	}

	@Override
	public FoodIntake add(FoodIntake obj) {
		return foodIntakeDao.add(obj);
	}

	@Override
	public void update(FoodIntake obj) {
		foodIntakeDao.update(obj);

	}

	@Override
	public void delete(FoodIntake obj) {
		foodIntakeDao.delete(obj);

	}

}
