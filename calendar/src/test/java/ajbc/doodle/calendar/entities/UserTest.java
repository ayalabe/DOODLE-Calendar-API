package ajbc.doodle.calendar.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class UserTest {

	private User user;
	
	private static final int ID = 100;
	private static final String FIRST_NAME = "Ayala", LAST_NAME = "Maskalchi",EMAIL = "ayalla120120@gmail.com";
	private static final LocalDate BIRTHDATE = LocalDate.of(1999, 10, 31);
	private static final boolean IS_LOGGED_IN = true, DISCONTINU = false;
	private static final Set<Event> EVENTS = new HashSet<Event>();
	private static final Set<Notification> NOTIFICATIONS = new HashSet<Notification>();
	
	public UserTest() {
		user = new User();
		user.setUserId(ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setEmail(EMAIL);
		user.setBirthdate(BIRTHDATE);
		user.loggIn(IS_LOGGED_IN);
		user.isDiscontinued(DISCONTINU);
		
		EVENTS.add(new Event());
		user.setEvents(EVENTS);
		NOTIFICATIONS.add(new Notification());

	}
	
	@Test
	void testFieldsValidation() {
		assertEquals(ID, user.getUserId());
		assertEquals(FIRST_NAME, user.getFirstName());
		assertEquals(LAST_NAME, user.getLastName());
		assertEquals(EMAIL, user.getEmail());
		assertEquals(BIRTHDATE, user.getBirthdate());
		assertEquals(1, user.getIsLogin());
		assertEquals(0, user.getDiscontinued());
	}
	
}