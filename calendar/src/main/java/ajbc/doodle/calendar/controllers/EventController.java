package ajbc.doodle.calendar.controllers;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.UserService;

@RequestMapping("/events")
@RestController
public class EventController {

	@Autowired
	EventService service;
	@Autowired
	UserService userService;

	// Create Event
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addEvent(@RequestBody Event event) {

		try {
			service.addEvent(event);
			event = service.getEvent(event.getEventId());
			return ResponseEntity.status(HttpStatus.CREATED).body(event);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add Event to db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	// Get event by id
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getEventById(@PathVariable Integer id) {

		Event event;
		try {
			event = service.getEvent(id);
			return ResponseEntity.ok(event);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to get event with id " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
		}
	}

	// Get all events
//	@RequestMapping(method = RequestMethod.GET)
//	public ResponseEntity<List<Event>> getAllEvents() throws DaoException {
//		List<Event> list;
//		list = service.getAllEvent();
//		if (list == null)
//			return ResponseEntity.notFound().build();
//
//		return ResponseEntity.ok(list);
//	}

	// Get with param
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getUserssIds(@RequestParam Map<String, String> map) throws DaoException {

		Set<String> keys = map.keySet();
		User user = null;
		// Get all users of an event by event id
		if(keys.contains("userId")) {
			
			user = userService.getUser(Integer.parseInt(map.get("userId")));
			
			if (user == null)
				return ResponseEntity.notFound().build();
			Set<Event> newList = user.getEvents();
			return ResponseEntity.ok(newList);
		}

		// Get all user that have an event between start date and time to end date and time.
		if (keys.contains("userId") && keys.contains("upcoming")) {
			LocalDateTime nowadate = LocalDateTime.now();
			List<Event> eventList = service.getEventByUserAndDate(Integer.parseInt(map.get("userId")), nowadate);
			return ResponseEntity.ok(eventList);
		}
		
		// Get events of a user in a range between start date and time to end date and time.
		// TODO 
		if (keys.contains("start") && keys.contains("end")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
			LocalDateTime start = LocalDateTime.parse(map.get("start"), formatter);
			LocalDateTime end = LocalDateTime.parse(map.get("end"), formatter);
			List<User> usersList = userService.getUsersByTimeRange(start, end);
			return ResponseEntity.ok(usersList);
		}
		
		// Get all events
		List<Event> list;
		list = service.getAllEvent();
		if (list == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(list);
	}




}