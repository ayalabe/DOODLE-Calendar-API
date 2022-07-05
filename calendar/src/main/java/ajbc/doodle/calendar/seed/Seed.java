package ajbc.doodle.calendar.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
public class Seed {

	
	@Autowired
	private UserService userService;
	private EventService eventService;
	


	@EventListener
	public void seed(ContextRefreshedEvent event) {
		try {
			seedUsersTable();
			seedEventTable();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void seedUsersTable() throws DaoException {

		User user = new User();
		user.setFirstName("Zelda");
		user.setLastName("Choen");
		user.setEmail("zzz@test.com");
		user.setBirthdate(LocalDate.of(1999, 10, 31));
		user.setJoinDate(LocalDate.now());
		user.setDiscontinued(0);
//		userService.addUser(user);
//
//		userService.getAllUser().stream().forEach(System.out::println);

	}

	
	private void seedEventTable() throws DaoException {

		Event event = new Event();
		event.setOwnerId(1000);
		event.setTitle("Merri");
		event.setAllDay(false);
		event.setStart(LocalDateTime.of(2022, 10, 31,10,50));
		event.setEnd(LocalDateTime.of(2022, 10, 31,00,50));
		event.setAddress("Jerusalem");
		event.setDescription("Good");
		event.setDiscontinued(0);
//		event.setGuests(Arrays.asList(1001));
//		event.setNotification(new ArrayList<Integer>());
//		eventService.addEvent(event.getOwnerId(), event);
//
//		eventService.getAllEvent().stream().forEach(System.out::println);

	}


}