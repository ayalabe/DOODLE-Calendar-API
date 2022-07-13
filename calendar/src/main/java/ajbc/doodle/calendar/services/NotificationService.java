package ajbc.doodle.calendar.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.NotificationDao;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;


@Service
public class NotificationService {

	@Autowired
	@Qualifier("htNotificationDao")
	NotificationDao notificationDao;
	@Autowired
	@Qualifier("htEventDao")
	EventDao eventDao;

	public void addNotification(Notification notification) throws DaoException{
		Event event = eventDao.getEvent(notification.getEventId());
		// Check if event is exist in db.
		if(event == null)
			throw new DaoException("Event no exist in DB");
		if(!isUserInEvent(event,notification.getUserId()))
			throw new DaoException("This user not exist in this event");
		notification.setEvent(event);
		notificationDao.addNotification(notification);
	}

	public void updateNotification(Notification notification) throws DaoException {
		notificationDao.updateNotification(notification);
	}

	public Notification getNotification(Integer notificationId) throws DaoException {
		return notificationDao.getNotification(notificationId);
	}

	//	public void deleteNotification(Integer eventId) throws DaoException {
	//		notificationDao.deleteEvent(eventId);
	//	}

	public List<Notification> getAllNotification() throws DaoException{
		return notificationDao.getAllNotification();
	}

	// check if the user is in the events
	private boolean isUserInEvent(Event event, Integer userId) {

		if(event == null)
			return false;

		if (event.getOwnerId().equals(userId)) 
			return true;

		List <User> users = new ArrayList<User>(event.getGuests());
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUserId().equals(userId)) 
				return true;	
		}

		return false;
	}
}
