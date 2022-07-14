package ajbc.doodle.calendar.daos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
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
	public List<Event> getEventByUserAndDate(Integer userId, LocalDateTime date) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		Criterion criterion1 = Restrictions.gt("start", date);
		criteria.add(criterion1);
		
		List<Event> events = (List<Event>)template.findByCriteria(criteria);
		
		return chechIfUserInEvent(events, userId);
	}
	
	@Override
	public List<Event> getAllEventsByTimeRange(LocalDateTime start, LocalDateTime end)throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		Criterion criterion1 = Restrictions.between("start", start, end);
		Criterion criterion2 = Restrictions.between("end", start, end);
		criteria.add(criterion1);
		criteria.add(criterion2);
		
		List<Event> events = (List<Event>)template.findByCriteria(criteria);
		
		Set<Event> eventsSet = new HashSet<Event>();
		eventsSet.addAll(events);
		
		return new ArrayList<Event>(eventsSet);
	}
	
	@Override
	public List<Event> getEventsByTimeRange(Integer userId, LocalDateTime start, LocalDateTime end) throws DaoException {
	
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		Criterion criterion1 = Restrictions.between("start", start, end);
		Criterion criterion2 = Restrictions.between("end", start, end);
		criteria.add(criterion1);
		criteria.add(criterion2);
		List<Event> events = (List<Event>)template.findByCriteria(criteria);
		
		return chechIfUserInEvent(events, userId);
	}
	
	
	@Override
	public List<Event> getEventsByOwnerId(Integer userId) throws DaoException {
	
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		Criterion criterion1 = Restrictions.eq("ownerId", userId);
		criteria.add(criterion1);
		List<Event> events = (List<Event>)template.findByCriteria(criteria);
		
		return events;
	}
	
	private List<Event> chechIfUserInEvent(List<Event> events, Integer userId){
		
		Set<Event> eventsSet = new HashSet<Event>();
		for (int i = 0; i < events.size(); i++) {
			Set<User> currentGests = events.get(i).getGuests();
			List<User> currentGestsList = new ArrayList<User>(currentGests);
			for (int j = 0; j < currentGestsList.size(); j++) {
				if(currentGestsList.get(j).getUserId().equals(userId))
					eventsSet.add(events.get(i));
			}
				
		}
		
		return new ArrayList<Event>(eventsSet);
	}
	
	@Override
	public void deleteSoftEvent(Integer eventId) throws DaoException {
		Event ev = getEvent(eventId);
		ev.setDiscontinued(1);
		updateEvent(ev);
	}

	@Override
	public void deleteEvent(Integer eventId) throws DaoException {
		Event ev = getEvent(eventId);
		template.delete(ev);
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
