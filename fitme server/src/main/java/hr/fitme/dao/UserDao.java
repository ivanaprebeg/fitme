package hr.fitme.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import hr.fitme.domain.User;

@Repository
public class UserDao extends CommonDao<User, Integer> implements IUserDao {

	@Override
	public User filterByUsername(String username) {
		CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();

		CriteriaQuery<User> q = cb.createQuery(User.class);
		Root<User> c = q.from(User.class);
		q.where(cb.equal(c.get("username"), username));
		try {
		User user = getCurrentSession().createQuery(q).getSingleResult();
		return user;
		}
		catch (Exception e) {
			return null;
			
		}
	}

}
