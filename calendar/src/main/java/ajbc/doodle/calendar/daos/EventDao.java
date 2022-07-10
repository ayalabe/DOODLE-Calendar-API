package ajbc.doodle.calendar.daos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;




@Transactional(rollbackFor = {DaoException.class}, readOnly = true)
public interface EventDao {

	//CRUD operations
	@Transactional(readOnly = false)
	public default void addEvent(Event even) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	@Transactional(readOnly = false)
	public default void updateEvent(Event even) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default Event getEvent(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	@Transactional(readOnly = false)
	public default void deleteEvent(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	//Queries
	
	// QUERIES
		public default List<Event> getAllEvent() throws DaoException {
			throw new DaoException("Method not implemented");
		}

		public default List<Event> getEventssByTimeRange(LocalDateTime start, LocalDateTime end) throws DaoException {
			throw new DaoException("Method not implemented");
		}
		public default List<Event> getEventByUserAndDate(Integer userId, LocalDateTime date) throws DaoException {
			throw new DaoException("Method not implemented");
		}
}
