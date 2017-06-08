package hr.fitme.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import hr.fitme.domain.Walk;

@Repository
public class WalkDao extends CommonDao<Walk, Integer> implements IWalkDao {

	@Override
	public List<Walk> getByUser(int userId) {
		List<Walk> walks = new ArrayList<Walk>();
		
		CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();

		CriteriaQuery<Walk> q = cb.createQuery(Walk.class);
		Root<Walk> c = q.from(Walk.class);
		q.where(cb.equal(c.get("user").get("id"), userId));
		q.orderBy(cb.asc(c.get("date")));
		walks.addAll(getCurrentSession().createQuery(q).getResultList());
		return walks;
	}

	@Override
	public Walk todayWalk(int userId) {
		CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
		
		CriteriaQuery<Walk> q = cb.createQuery(Walk.class);
		Root<Walk> c = q.from(Walk.class);
		Date today = new Date(Calendar.getInstance().getTime().getTime());
		q.where(cb.and(cb.equal(c.get("user").get("id"), userId), 
				cb.equal(c.get("date"), today)));
		return getCurrentSession().createQuery(q).getSingleResult();
	}
}
