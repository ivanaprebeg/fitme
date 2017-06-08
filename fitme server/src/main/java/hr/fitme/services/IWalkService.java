package hr.fitme.services;

import java.util.List;

import hr.fitme.dao.ICommonDao;
import hr.fitme.domain.FoodCategory;
import hr.fitme.domain.User;
import hr.fitme.domain.Walk;
import hr.fitme.dto.WalkDto;

public interface IWalkService extends ICommonDao<Walk, Integer> {
	
	public List<Walk> walksByUser(User user);
	public Walk todayWalk(User user);

}
