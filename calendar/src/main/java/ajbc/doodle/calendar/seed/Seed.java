package ajbc.doodle.calendar.seed;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.RepeatingOptions;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;


@Component
public class Seed {

	
	@Autowired
	private UserService userService;
	@Autowired
	private EventService eventService;
	


	@EventListener
	public void seed(ContextRefreshedEvent event) {
		try {
//			seedUsersTable();
			seedEventTable();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		userService.getAllUser().stream().forEach(System.out::println);

	}

	
	private void seedEventTable() throws DaoException {

		Event event = new Event();
		event.setOwnerId(1000);
		event.setTitle("exam");
		event.setIsAllDay(1);
		event.setStart(LocalDateTime.of(2022, 10, 31,10,50));
		event.setEnd(LocalDateTime.of(2022, 10, 31,00,50));
		event.setAddress("Jerusalem");
		event.setDescription("nice");
		event.setDiscontinued(0);
		event.setRepeating(RepeatingOptions.NONE);
		event.setGuests(Arrays.asList(userService.getUser(1001)));
		
		eventService.addEvent(event);

		eventService.getAllEvent().stream().forEach(System.out::println);

	}
	


}