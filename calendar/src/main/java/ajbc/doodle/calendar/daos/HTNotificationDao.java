package ajbc.doodle.calendar.daos;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.manager.NotificationManager;

@SuppressWarnings("unchecked")
@Repository("htNotificationDao")
public class HTNotificationDao implements NotificationDao {

	@Autowired
	private HibernateTemplate template;
	@Autowired
	private NotificationManager manager;

	//CRUD operations
	@Override
	public void addNotification(Notification notification) throws DaoException {
		template.persist(notification);
		manager.addQueue(notification);
	}

	
	@Override
	public void updateNotification(Notification notification) throws DaoException {
		template.merge(notification);
		manager.addQueue(notification);
	}
	@Override
	public Notification getNotification(Integer notificationId) throws DaoException {
		Notification ev = template.get(Notification.class, notificationId);
		if (ev ==null)
			throw new DaoException("No Such Notification in DB");
		return ev;
	}
	@Override
	public void deleteSoftNotification(Integer notificationId) throws DaoException {
		Notification notification = getNotification(notificationId);
		notification.setDiscontinued(1);
		updateNotification(notification);
	}
	
	@Override
	public void deleteNotification(Integer notificationId) throws DaoException {
		Notification notification = getNotification(notificationId);
		template.delete(notification);
	}

		@Override
		public List<Notification> getAllNotification() throws DaoException {
			DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
			List<Notification> notifList = (List<Notification>)template.findByCriteria(criteria);
			if(notifList==null)
				throw new DaoException("No Notification found in DB");
			return notifList;
		}
		
		@Override
		public List<Notification> getAllNotificationNotSend() throws DaoException {
			DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
			Criterion criterion1 = Restrictions.eq("isSend", 0);
			criteria.add(criterion1);
			
			List<Notification> notifications = (List<Notification>)template.findByCriteria(criteria);
			
			return notifications;
		}

}
