package hr.fitme.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import hr.fitme.domain.UserWeight;

@Repository
public class UserWeightDao extends CommonDao<UserWeight, Integer> implements IUserWeightDao{

	@Override
	public List<UserWeight> getForUser(int userId) {
		List<UserWeight> walks = new ArrayList<UserWeight>();
		
		CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();

		CriteriaQuery<UserWeight> q = cb.createQuery(UserWeight.class);
		Root<UserWeight> c = q.from(UserWeight.class);
		q.where(cb.equal(c.get("user").get("id"), userId));
		q.orderBy(cb.asc(c.get("date")));
		walks.addAll(getCurrentSession().createQuery(q).getResultList());
		return walks;
	}
}
