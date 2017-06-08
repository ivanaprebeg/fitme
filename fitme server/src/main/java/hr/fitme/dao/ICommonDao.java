package hr.fitme.dao;

import java.io.Serializable;
import java.util.List;

public interface ICommonDao<T extends Serializable, ID extends Serializable> {
	public List<T> getAll() ;

	public T getById(ID id) ;

	public T add(T obj) ;

	public void update(T obj) ;

	public void delete(T obj) ;
}
