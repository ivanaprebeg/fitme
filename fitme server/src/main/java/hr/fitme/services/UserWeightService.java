package hr.fitme.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.fitme.dao.IUserWeightDao;
import hr.fitme.domain.User;
import hr.fitme.domain.UserWeight;


@Transactional
@Service
public class UserWeightService implements IUserWeightService {
	
	@Autowired
	private IUserWeightDao weightDao;

	@Override
	public List<UserWeight> getAll() {
		return weightDao.getAll();
	}

	@Override
	public UserWeight getById(Integer id) {
		return weightDao.getById(id);
	}

	@Override
	public UserWeight add(UserWeight obj) {
		return weightDao.add(obj);
	}

	@Override
	public void update(UserWeight obj) {
		weightDao.update(obj);
		
	}

	@Override
	public void delete(UserWeight obj) {
		weightDao.delete(obj);
		
	}

	@Override
	public List<UserWeight> getForUser(User user) {
		return weightDao.getForUser(user.getId());
	}

}
