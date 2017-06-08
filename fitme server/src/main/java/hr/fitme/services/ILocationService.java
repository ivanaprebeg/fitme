package hr.fitme.services;

import java.util.List;

import hr.fitme.dao.ICommonDao;
import hr.fitme.domain.Location;
import hr.fitme.domain.User;

public interface ILocationService extends ICommonDao<Location, Integer>{
	
	List<Location> getLocations(User user);

}
