package ajbc.doodle.calendar.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import ajbc.doodle.calendar.entities.Notification;

@TestInstance(Lifecycle.PER_METHOD)
class NotificationManagerTest {

	private NotificationManager manager;


	public NotificationManagerTest() {
		manager = new NotificationManager();
	}

	@Test
	void testAddNotification() throws InterruptedException {
		Notification notification = new Notification();
		LocalDateTime now = LocalDateTime.now();

		notification.setLocalDateTime(now);

		manager.addQueue(notification);

		assertEquals(now, manager.getDateTime());

		Thread.sleep(500);
		assertTrue(manager.getNotificationsQueue().isEmpty());

		LocalDateTime inOneHour = LocalDateTime.now().plusHours(1);
		notification.setLocalDateTime(inOneHour);
		manager.addQueue(notification);
		assertEquals(inOneHour, manager.getDateTime());
		assertTrue(manager.getNotificationsQueue().peek() == notification);
	}

	@Test
	void tesTtimeToSleep() {
		LocalDateTime inOneSecond = LocalDateTime.now().plusSeconds(1);
		assertTrue(manager.timeToSleep(inOneSecond) <= 1);

		LocalDateTime in60Seconds = LocalDateTime.now().plusSeconds(60);
		long seconds = manager.timeToSleep(in60Seconds);
		assertTrue(seconds > 55 && seconds <= 60);

		LocalDateTime oneSecondBeforeNow = LocalDateTime.now().minusSeconds(1);
		assertTrue(manager.timeToSleep(oneSecondBeforeNow) == 0);

		LocalDateTime oneHourBeforeNow = LocalDateTime.now().minusHours(1);
		assertTrue(manager.timeToSleep(oneHourBeforeNow) == 0);
	}

	
}