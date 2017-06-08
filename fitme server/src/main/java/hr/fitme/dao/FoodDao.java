package hr.fitme.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import hr.fitme.domain.Food;
import hr.fitme.domain.FoodCategory;

@Repository
public class FoodDao extends CommonDao<Food, Integer> implements IFoodDao {

	@Override
	public List<Food> findByCategory(FoodCategory category) {

		CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();

		CriteriaQuery<Food> q = cb.createQuery(Food.class);
		Root<Food> c = q.from(Food.class);
		q.where(cb.equal(c.get("category"), category));
		try {
			List<Food> food = getCurrentSession().createQuery(q).getResultList();
			return food;
		} catch (Exception e) {
			return null;
		}
	}
}
