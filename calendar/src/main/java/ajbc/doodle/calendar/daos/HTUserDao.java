package ajbc.doodle.calendar.daos;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.User;

@SuppressWarnings("unchecked")
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
		if (users.get(0) ==null)
			throw new DaoException("No Such User in DB");
		return users.get(0);
	}
	
	@Override
	public void deleteUser(Integer userId) throws DaoException {
		User us = getUser(userId);
		us.setDiscontinued(1);
		updateUser(us);
	}

		@Override
		public List<User> getAllUser() throws DaoException {
			DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
			List<User> userList = (List<User>)template.findByCriteria(criteria);
			if(userList==null)
				throw new DaoException("No users found in DB");
			return userList;
		}

}
