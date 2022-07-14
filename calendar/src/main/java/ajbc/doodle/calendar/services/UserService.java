package ajbc.doodle.calendar.services;

import java.time.LocalDateTime;
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
import ajbc.doodle.calendar.entities.webpush.Subscription;


@Service
public class UserService {

	@Autowired
	@Qualifier("htUserDao")
	UserDao userDao;
	@Autowired
	@Qualifier("htEventDao")
	 EventDao eventDao;
	@Autowired
	@Qualifier("htNotificationDao")
	 NotificationDao notificationDao;
	@Autowired
	
	 EventService eventService;
	
	public void addUser(User user) throws DaoException{
		userDao.addUser(user);
	}
	
	public void addListUsers(List<User> users) throws DaoException{
		userDao.addListUsers(users);
	}
	
	public void updateUser(User user) throws DaoException {
		userDao.updateUser(user);
	}
	
	
	public synchronized User getUser(Integer userId) throws DaoException {
		return userDao.getUser(userId);
	}
	
	public User getUserByEmail(String email) throws DaoException {
		return userDao.getUserByEmail(email);
	}
	
	public List<User> getUsersByTimeRange(LocalDateTime start, LocalDateTime end) throws DaoException {
		return userDao.getUsersByTimeRange(start, end);
	}
	
	public void deleteUser(Integer userId) throws DaoException {
		User user = getUser(userId);
		user.isDiscontinued(true);
		user.loggIn(false);
		userDao.deleteUser(user);
	}
	
	public void deleteUserHard(Integer userId) throws DaoException {
		
		List<Event> listEvents = eventDao.getEventsByOwnerId(userId);
		for (int i = 0; i < listEvents.size(); i++) {
			eventService.deleteEvent(listEvents.get(i).getEventId());
		}
		User user = getUser(userId);
		
		user.getEvents().forEach(event -> event.getGuests().remove(user));
		
		userDao.updateUser(user);
		
		List<Notification> notifications = notificationDao.getAllNotificationNotByUserId(userId);
		for (int i = 0; i < notifications.size(); i++) {
			notificationDao.deleteNotification(notifications.get(i).getNotificationId()); 
		}
		
		userDao.deleteUserHard(userDao.getUser(userId));
	}
	
	public List<User> getAllUser() throws DaoException{
		return userDao.getAllUser();
	}

	//log in 	
			public void login(User user, Subscription subscription) throws DaoException {
				user.loggIn(true);
				
				user.setEndPointLog(subscription.getEndpoint());
				user.setKeys(subscription.getKeys().getP256dh());
				user.setAuth(subscription.getKeys().getAuth());
				
				userDao.updateUser(user);
			}
			
			public void logout(User user) throws DaoException {
				user.loggIn(false);
				userDao.updateUser(user);
			}

	
}
