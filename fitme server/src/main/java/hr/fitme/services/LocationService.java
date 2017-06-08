package hr.fitme.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.fitme.dao.ILocationDao;
import hr.fitme.domain.Location;
import hr.fitme.domain.User;

@Transactional
@Service
public class LocationService implements ILocationService {
	
	@Autowired
	ILocationDao locationDao;

	@Override
	public List<Location> getAll() {
		return locationDao.getAll();
	}

	@Override
	public Location getById(Integer id) {
		return locationDao.getById(id);
	}

	@Override
	public Location add(Location obj) {
		return locationDao.add(obj);
	}

	@Override
	public void update(Location obj) {
		locationDao.update(obj);
		
	}

	@Override
	public void delete(Location obj) {
		locationDao.delete(obj);
		
	}

	@Override
	public List<Location> getLocations(User user) {
		return locationDao.getLocations(user.getId());
	}

}
