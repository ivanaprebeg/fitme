package hr.fitme.dao;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import hr.fitme.domain.Location;

@Repository
public class LocationDao extends CommonDao<Location, Integer> implements ILocationDao {

	@Override
	public List<Location> getLocations(int userId) {
		CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();

		CriteriaQuery<Location> q = cb.createQuery(Location.class);
		Root<Location> c = q.from(Location.class);
		
		q.where(cb.equal(c.get("user").get("id"), userId));
		q.orderBy(cb.asc(c.get("date")));
		try {
		List<Location> locations = getCurrentSession().createQuery(q).getResultList();
		List<Location> result = new ArrayList<>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		for (Location loc : locations) {
			
			if (dtf.format(loc.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).equals(dtf.format(localDate)))
					result.add(loc);
		}
		return result;
		}
		catch (Exception e) {
			return null;
			
		}
	}

}
