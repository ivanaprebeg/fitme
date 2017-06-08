package hr.fitme.services;

import java.util.List;

import hr.fitme.dao.ICommonDao;
import hr.fitme.domain.Meal;
import hr.fitme.domain.User;
import hr.fitme.domain.UserWeight;

public interface IUserWeightService extends ICommonDao<UserWeight, Integer> {
	public List<UserWeight> getForUser(User user);
}
