package hr.fitme.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.fitme.auth.JwtUser;
import hr.fitme.auth.JwtUserFactory;
import hr.fitme.dao.IUserDao;
import hr.fitme.domain.User;

@Transactional
@Service
public class UserService implements IUserService, UserDetailsService {

	@Autowired
	private IUserDao userDao;

	@Override
	public List<User> getAll() {
		return userDao.getAll();
	}
	
	@Override
	public User findByUsername(String username) {
		User user = userDao.filterByUsername(username);
		return user;
	}
	
	@Override
	public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
		 User user = userDao.filterByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
	}

	@Override
	public User add(User user) {
		userDao.add(user);
		return user;
	}

	@Override
	public User updateUser(User user) {
		userDao.update(user);
		return user;
	}

}
