package hr.fitme.dao;

import java.util.List;

import hr.fitme.domain.Location;

public interface ILocationDao extends ICommonDao<Location, Integer> {
	public List<Location> getLocations(int userId);
}
