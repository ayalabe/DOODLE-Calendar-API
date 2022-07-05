package ajbc.doodle.calendar.daos;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.Category;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.EventGuests;
import ajbc.doodle.calendar.entities.Product;
import ajbc.doodle.calendar.entities.User;

@SuppressWarnings("unchecked")
@Repository("htEventDao")
public class HTEventDao implements EventDao {

	@Autowired
	private HibernateTemplate template;


	//CRUD operations
	@Override
	public void addEvent(Event event) throws DaoException {
		template.persist(event);
		List<Integer> guests = event.getGuests();
		for (int i = 0; i < guests.size(); i++) {
			addEventGuests(new EventGuests(event.getEventId(), guests.get(i)));
		}
	}
	private void addEventGuests(EventGuests eventGuests) {
		template.persist(eventGuests);
		
	}
	
	@Override
	public void updateEvent(Event event) throws DaoException {
		template.merge(event);
	}
	@Override
	public Event getEvent(Integer eventId) throws DaoException {
		Event ev = template.get(Event.class, eventId);
		if (ev ==null)
			throw new DaoException("No Such Event in DB");
		return ev;
	}
	@Override
	public void deleteEvent(Integer eventId) throws DaoException {
		Event ev = getEvent(eventId);
		ev.setDiscontinued(1);
		updateEvent(ev);
	}

		@Override
		public List<Event> getAllEvent() throws DaoException {
			DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
			List<Event> eventList = (List<Event>)template.findByCriteria(criteria);
			if(eventList==null)
				throw new DaoException("No event found in DB");
			return eventList;
		}

}
