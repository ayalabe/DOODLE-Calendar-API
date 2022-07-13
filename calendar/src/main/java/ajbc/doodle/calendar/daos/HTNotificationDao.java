package ajbc.doodle.calendar.daos;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.Event;
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
		try {
			manager.addQueue(notification);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Override
	public void updateNotification(Notification notification) throws DaoException {
		template.merge(notification);
	}
	@Override
	public Notification getNotification(Integer notificationId) throws DaoException {
		Notification ev = template.get(Notification.class, notificationId);
		if (ev ==null)
			throw new DaoException("No Such Notification in DB");
		return ev;
	}
//	@Override
//	public void deleteNotification(Integer notificationId) throws DaoException {
//		Event ev = getEvent(eventId);
//		ev.setDiscontinued(1);
//		updateEvent(ev);
//	}

		@Override
		public List<Notification> getAllNotification() throws DaoException {
			DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
			List<Notification> notifList = (List<Notification>)template.findByCriteria(criteria);
			if(notifList==null)
				throw new DaoException("No Notification found in DB");
			return notifList;
		}

}
