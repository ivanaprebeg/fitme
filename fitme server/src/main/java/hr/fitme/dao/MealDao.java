package hr.fitme.dao;


import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import hr.fitme.domain.Meal;

@Repository
public class MealDao extends CommonDao<Meal, Integer> implements IMealDao {

	@Override
	public List<Meal> getMealsToday(int userId) {
		CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();

		CriteriaQuery<Meal> q = cb.createQuery(Meal.class);
		Root<Meal> c = q.from(Meal.class);
		//TODO fix date
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date fromDate = new Date(calendar.getTime().getTime());

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date toDate = new Date(calendar.getTime().getTime());
		
		q.where(cb.between(c.get("date"), fromDate, toDate));
		q.where(cb.equal(c.get("user").get("id"), userId));
		q.orderBy(cb.asc(c.get("date")));
		try {
		List<Meal> meals = getCurrentSession().createQuery(q).getResultList();
		return meals;
		}
		catch (Exception e) {
			return null;
			
		}
	}



}
