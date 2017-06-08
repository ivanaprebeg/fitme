package hr.fitme.dao;

import java.util.List;

import hr.fitme.domain.Walk;

public interface IWalkDao extends ICommonDao<Walk, Integer> {
	
	public List<Walk> getByUser (int userId);
	public Walk todayWalk(int userId);
}
