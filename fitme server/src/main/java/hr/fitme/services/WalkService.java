package hr.fitme.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.fitme.dao.IWalkDao;
import hr.fitme.domain.User;
import hr.fitme.domain.Walk;
import hr.fitme.dto.WalkDto;

@Transactional
@Service
public class WalkService implements IWalkService {
	
	@Autowired
	IWalkDao walkDao;

	@Override
	public List<Walk> getAll() {
		return walkDao.getAll();
	}

	@Override
	public Walk getById(Integer id) {
		return walkDao.getById(id);
	}

	@Override
	public Walk add(Walk obj) {
		return walkDao.add(obj);
	}

	@Override
	public void update(Walk obj) {
		walkDao.update(obj);
		
	}

	@Override
	public void delete(Walk obj) {
		walkDao.delete(obj);
		
	}

	@Override
	public List<Walk> walksByUser(User user) {
		return walkDao.getByUser(user.getId());
	}

	@Override
	public Walk todayWalk(User user) {
		return walkDao.todayWalk(user.getId());
	}

}
