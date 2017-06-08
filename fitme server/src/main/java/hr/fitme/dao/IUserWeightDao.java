package hr.fitme.dao;

import java.util.List;

import hr.fitme.domain.UserWeight;

public interface IUserWeightDao extends ICommonDao<UserWeight, Integer>{
	
	public List<UserWeight> getForUser(int userId);
	
}
