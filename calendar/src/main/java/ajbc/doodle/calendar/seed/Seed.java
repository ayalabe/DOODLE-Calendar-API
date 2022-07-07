package ajbc.doodle.calendar.seed;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.RepeatingOptions;
import ajbc.doodle.calendar.entities.Unit;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.NotificationService;
import ajbc.doodle.calendar.services.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Component
public class Seed {

	
	@Autowired
	private UserService userService;
	@Autowired
	private EventService eventService;
	@Autowired
	private NotificationService notificationService;
	


	@EventListener
	public void seed(ContextRefreshedEvent event) {
		try {
//			seedUsersTable();
			seedEventTable();
//			seedNotificationTable();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void seedNotificationTable() throws DaoException {
		Notification notification = new Notification();
		notification.setEventId(1000);
		notification.setUserId(1000);
		notification.setEvent(eventService.getEvent(1000));
		notification.setLocalDateTime(LocalDateTime.of(2022, 10, 31,10,50));
		notification.setMessage("bbbbb");
		notification.setQuantity(3);
		notification.setTitle("mornung");
		notification.setUnit(Unit.HOURS);
//		Notification notification = new Notification(1, 1000, LocalDateTime.of(2022, 10, 31,10,50), 
//				"tizcoret", "runnn", Unit.HOURS, 3, eventService.getEvent(1000));
		
//		Notification notification = new Notification(1, 1000, LocalDateTime.of(2022, 10, 31,10,50), 
//				"tizcoret", "runnn", Unit.HOURS, 3);
		
//		notificationService.addNotification(notification);
		
	}


	private void seedUsersTable() throws DaoException {

		User user1 = new User();
		user1.setFirstName("zelda");
		user1.setLastName("rrrrr");
		user1.setEmail("zzzzz@test.com");
		user1.setBirthdate(LocalDate.of(1999, 10, 31));
		user1.setJoinDate(LocalDate.now());
		user1.setDiscontinued(0);
		user1.setIsLogin(1);
		userService.addUser(user1);
		
		User user2 = new User();
		user2.setFirstName("Ayala");
		user2.setLastName("Maskalchi");
		user2.setEmail("ayala@test.com");
		user2.setBirthdate(LocalDate.of(1999, 10, 31));
		user2.setJoinDate(LocalDate.now());
		user2.setDiscontinued(0);
		user2.setIsLogin(1);
		userService.addUser(user2);

//		userService.getAllUser().stream().forEach(System.out::println);

	}

	
	private void seedEventTable() throws DaoException {
		
		Notification notification = new Notification();
		notification.setEventId(1001);
		notification.setUserId(1000);
		notification.setEvent(eventService.getEvent(1001));
		notification.setLocalDateTime(LocalDateTime.of(2022, 10, 31,10,50));
		notification.setMessage("Good Morning");
		notification.setQuantity(3);
		notification.setTitle("mornung");
		notification.setUnit(Unit.HOURS);

		Event event1 = new Event();
		event1.setOwnerId(1000);
		event1.setTitle("srsr");
		event1.setIsAllDay(1);
		event1.setStart(LocalDateTime.of(2020, 10, 1,10,50));
		event1.setEnd(LocalDateTime.of(2020, 10, 2,12,00));
		event1.setAddress("Jerusalem");
		event1.setDescription("nice");
		event1.setDiscontinued(0);
		event1.setRepeating(RepeatingOptions.NONE);
		event1.setGuests(Arrays.asList(userService.getUser(1001),userService.getUser(1002)));
		
		Set<Notification> notifications = new HashSet<Notification>();
		notifications.add(notification);
		event1.setNotifications(notifications);
//		notifications.addAll(Arrays.asList(new Notification(1, 1000, LocalDateTime.of(2022, 10, 31,10,50), 
//				"tizcoret", "runnn", Unit.HOURS, 3, eventService.getEvent(1000))));
//		
//		Event event2 = new Event(1,1001, "Merry", 1,LocalDateTime.of(2022, 10, 31,10,50),LocalDateTime.of(2022, 10, 31,00,50),
//				"Hifa", "nicee",0,RepeatingOptions.NONE, Arrays.asList(userService.getUser(1001)),
//				notifications);
//		
//		
//		eventService.addEvent(event1);



	}
	


}