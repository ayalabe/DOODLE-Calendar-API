package ajbc.doodle.calendar.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;


@Service
public class EventService {

	@Autowired
	@Qualifier("htEventDao")
	EventDao eventDao;
	@Autowired
	@Qualifier("htUserDao")
	UserDao userDao;
	
	public void addEvent(Event event) throws DaoException{
//		User user = userDao.getUser(event.getOwnerId());
//		// Check if user owner is exist in db.
//		if(user == null)
//			throw new DaoException("User no exist in DB");
		eventDao.addEvent(event);
	}
	
	public void updateUser(Event event) throws DaoException {
		eventDao.updateEvent(event);
	}

	public Event getUser(Integer userId) throws DaoException {
		return eventDao.getEvent(userId);
	}
	
	public void deleteEvent(Integer eventId) throws DaoException {
		eventDao.deleteEvent(eventId);
	}
	
	public List<Event> getAllEvent() throws DaoException{
		return eventDao.getAllEvent();
	}
}
