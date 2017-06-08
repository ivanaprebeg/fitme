package hr.fitme.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CommonDao<T extends Serializable, ID extends Serializable> implements ICommonDao<T, ID> {

	private Class<T> clazz;

	@Autowired
	SessionFactory sessionFactory;

	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public CommonDao() {
		this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	public List<T> getAll() {
		CriteriaQuery<T> cq = getCurrentSession().getCriteriaBuilder().createQuery(clazz);
		cq.from(clazz);
		return getCurrentSession().createQuery(cq).getResultList();
	}

	@Override
	public T getById(ID id) {
		return (T) getCurrentSession().get(clazz, id);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public T add(T obj) {
		return getById((ID)getCurrentSession().save(obj));
	}

	@Override
	public void update(T obj) {
		getCurrentSession().update(obj);
	}

	@Override
	public void delete(T obj) {
		getCurrentSession().delete(obj);
	}

}
