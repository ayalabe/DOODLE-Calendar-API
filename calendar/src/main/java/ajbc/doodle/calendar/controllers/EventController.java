package ajbc.doodle.calendar.controllers;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
//	@RequestMapping(method = RequestMethod.POST)
//	public ResponseEntity<?> addEvent(@RequestBody Event event) {
//
//		try {
//			service.addEvent(event);
//			event = service.getEvent(event.getEventId());
//			return ResponseEntity.status(HttpStatus.CREATED).body(event);
//		} catch (DaoException e) {
//			ErrorMessage errorMessage = new ErrorMessage();
//			errorMessage.setData(e.getMessage());
//			errorMessage.setMessage("failed to add Event to db");
//			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
//		}
//	}
	
	
	@PostMapping("/{id}")
	public ResponseEntity<?> addEvent(@RequestBody List<Event> events, @PathVariable Integer id) {

		List<Event> eventsList=new ArrayList<Event>();
		try {
			for (Event event2 : events) {
				service.addEvent(event2);
				eventsList.add(service.getEvent(event2.getEventId()));
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(eventsList);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add event to db");
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


	// Get with param
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getUserssIds(@RequestParam Map<String, String> map) throws DaoException {

		Set<String> keys = map.keySet();
		User user = null;

		// Get upcoming events of a user ( only future events )
		if (keys.contains("userId") && keys.contains("upcoming")) {
			LocalDateTime nowadate = LocalDateTime.now();
			List<Event> eventList = service.getEventByUserAndDate(Integer.parseInt(map.get("userId")), nowadate);
			return ResponseEntity.ok(eventList);
		}


		// Get events of a user the next coming num of minutes and hours
		if (keys.contains("userId") && keys.contains("num") && keys.contains("hours")) {
			LocalDateTime nowadate = LocalDateTime.now();
			LocalDateTime dateAfter = nowadate.plusHours(Long.parseLong(map.get("hours")));
			dateAfter = dateAfter.plusMinutes(Long.parseLong(map.get("num")));
			System.out.println(dateAfter);
			List<Event> eventList = service.getEventsByTimeRange(Integer.parseInt(map.get("userId")),nowadate, dateAfter);
			return ResponseEntity.ok(eventList);
		}

		// Get events of a user in a range between start date and time to end date and time. 
		if (keys.contains("userId") && keys.contains("start") && keys.contains("end")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
			LocalDateTime start = LocalDateTime.parse(map.get("start"), formatter);
			LocalDateTime end = LocalDateTime.parse(map.get("end"), formatter);
			List<Event> usersList = service.getEventsByTimeRange(Integer.parseInt(map.get("userId")),start, end);
			return ResponseEntity.ok(usersList);
		}

		// Get all events in a range between start date and time to end date and time.
		if (keys.contains("start") && keys.contains("end")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
			LocalDateTime start = LocalDateTime.parse(map.get("start"), formatter);
			LocalDateTime end = LocalDateTime.parse(map.get("end"), formatter);
			List<Event> usersList = service.getAllEventsByTimeRange(start, end);
			return ResponseEntity.ok(usersList);
		}



		// Get all events of a user
		if(keys.contains("userId")) {

			user = userService.getUser(Integer.parseInt(map.get("userId")));

			if (user == null)
				return ResponseEntity.notFound().build();
			Set<Event> newList = user.getEvents();
			return ResponseEntity.ok(newList);
		}

		// Get all events
		List<Event> list;
		list = service.getAllEvent();
		if (list == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(list);
	}

	// Update Event
	@RequestMapping(method = RequestMethod.PUT, path="/{id}")
	public ResponseEntity<?> updateProduct(@RequestBody Event event, @PathVariable Integer id) {

		try {
			event.setEventId(id);
			service.updateEvent(event);
			event = service.getEvent(event.getEventId());
			return ResponseEntity.status(HttpStatus.OK).body(event);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to update Event in db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	// Soft Delete Event
//	@RequestMapping(method = RequestMethod.DELETE, path="/{id}")
//	public ResponseEntity<?> deleteUser(@PathVariable Integer id, @RequestParam Map<String, String> map) {
//		Set<String> keys = map.keySet();
//		Event event = null;
//		try {
//			if(keys.contains("soft")) {
//				event = service.getEvent(id);
//				service.deleteSoftEvent(id);
//				event = service.getEvent(id);
//				
//			}
//			if(keys.contains("hard")) {
//				System.out.println("hhhh");
//				service.deleteEvent(id);
//				return ResponseEntity.status(HttpStatus.OK).body(event);
//			}
//
//
//		} catch (DaoException e) {
//			ErrorMessage errorMessage = new ErrorMessage();
//			errorMessage.setData(e.getMessage());
//			errorMessage.setMessage("failed to delete Event from db");
//			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
//		}
//		return ResponseEntity.status(HttpStatus.OK).body(event);
//
//	}
	
	
	@DeleteMapping
	public ResponseEntity<List<Event>> DeleteEvent(@RequestBody List<Integer> eventsIds, @RequestParam Map<String, String> map)
			throws DaoException {
		Set<String> keys = map.keySet();

		if (keys.contains("soft"))
			for (Integer evId : eventsIds) {
				service.deleteSoftEvent(evId);
			}
		if (keys.contains("hard"))
			for (Integer evId : eventsIds) {
				service.deleteEvent(evId);
			}
			

		return ResponseEntity.status(HttpStatus.OK).build();
	}
	

}