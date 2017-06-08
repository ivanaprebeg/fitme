package hr.fitme.dao;

import hr.fitme.domain.User;

public interface IUserDao extends ICommonDao<User, Integer> {
	public User filterByUsername(String username);
}
