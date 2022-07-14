package ajbc.doodle.calendar.daos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;

@SuppressWarnings("unchecked")
@Component
@Repository("htUserDao")
public class HTUserDao implements UserDao {

	@Autowired
	private HibernateTemplate template;


	//CRUD operations
	@Override
	public void addUser(User user) throws DaoException {
		template.persist(user);
	}
	
	@Override
	public void addListUsers(List<User> users) throws DaoException {
		for (int i = 0; i < users.size(); i++) {
			template.persist(users.get(i));
		}
	}
	@Override
	public void updateUser(User user) throws DaoException {
		template.merge(user);
	}
	
	@Override
	public User getUser(Integer userId) throws DaoException {
		User us = template.get(User.class, userId);
		if (us ==null)
			throw new DaoException("No Such User in DB");
		return us;
	}
	@Override
	public User getUserByEmail(String email) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("email", email));
		List<User> users = (List<User>) template.findByCriteria(criteria);
		if (users.isEmpty())
			return null;
		return users.get(0);
	}
	
	@Override
	public List<User> getUsersByTimeRange(LocalDateTime start, LocalDateTime end) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		Criterion criterion1 = Restrictions.between("start", start, end);
		Criterion criterion2 = Restrictions.between("end", start, end);
		criteria.add(criterion1);
		criteria.add(criterion2);
		List<Event> events = (List<Event>)template.findByCriteria(criteria);
		HashSet<User> setUser = new HashSet<>();
		
		for (int i = 0; i < events.size(); i++) {
			setUser.add(getUser(events.get(i).getOwnerId()));
			setUser.addAll(events.get(i).getGuests());
		}
		List<User> users = new ArrayList<User>(setUser);
		return users;
	}
	
	@Override
	public void deleteUser(User user) throws DaoException {
		updateUser(user);
	}
	
	@Override
	public void deleteUserHard(User user) throws DaoException {
		template.delete(user);
	}

		@Override
		public List<User> getAllUser() throws DaoException {
			DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
			List<User> userList = (List<User>)template.findByCriteria(criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
			if(userList==null)
				throw new DaoException("No users found in DB");
			return userList;
		}

}
