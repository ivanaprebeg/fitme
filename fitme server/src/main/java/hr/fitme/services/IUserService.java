package hr.fitme.services;

import java.util.List;

import hr.fitme.auth.JwtUser;
import hr.fitme.dao.IUserDao;
import hr.fitme.domain.User;

public interface IUserService  {

	public List<User> getAll() ;

	public User findByUsername(String username);
	
	public User add(User user);
	
	public User updateUser(User user);
}
