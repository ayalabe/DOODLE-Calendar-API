package ajbc.doodle.calendar.manager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.NotificationService;
import ajbc.doodle.calendar.services.UserService;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Component
public class NotificationManager {

	@Autowired
	UserService userService;
	@Autowired
	NotificationService notificationService;
	@Autowired
	MessagePushService messagePushService;


	@EventListener
	public void start(ContextRefreshedEvent event) {
		try {
			fetchNotificationFromDB();
			if(!notificationsQueue.isEmpty()) {
				createThread(notificationsQueue.peek().getLocalDateTime());
			}
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private final int NUM_THREAD_M = 1;
	private LocalDateTime dateTime = null;
	private ScheduledExecutorService executorManager = Executors.newScheduledThreadPool(NUM_THREAD_M);
	public Queue<Notification> notificationsQueue = new PriorityQueue<Notification>(new Comparator<Notification>() {


		@Override
		public int compare(Notification n1, Notification n2) {

			if(n1.getLocalDateTime().equals(n2.getLocalDateTime()))
				return 0;

			else if(n1.getLocalDateTime().isAfter(n2.getLocalDateTime()))
				return 1;

			return -1;
		}});


	long timeToSleep(LocalDateTime dateTime) {
		long secondsToSleep = LocalDateTime.now().until(dateTime, ChronoUnit.SECONDS);
		return secondsToSleep < 0 ? 0 : secondsToSleep;
	}
	
	public void addQueue(Notification notification) {
		notificationsQueue.add(notification);
		System.out.println("add notifi");
		if (dateTime == null || notification.getLocalDateTime().isBefore(dateTime)) {
			System.out.println(notification.getLocalDateTime());
			createThread(notification.getLocalDateTime());
		}
	}


	private void createThread(LocalDateTime time) {

		this.dateTime = time;

		long secondsToSleep = timeToSleep(time);

		if (executorManager != null) {
			executorManager.shutdownNow();
		}

		executorManager = Executors.newScheduledThreadPool(NUM_THREAD_M);
		executorManager.schedule(managerQueue, secondsToSleep, TimeUnit.SECONDS);
	}


	private void fetchNotificationFromDB() throws DaoException {
		List<Notification> notifications = notificationService.getAllNotificationNotSend();
		notificationsQueue.addAll(notifications);
	}
	
	public void removeFromQueue(Notification notification) {
		notificationsQueue.remove(notification);
	}
	

	public Runnable managerQueue = () -> {
		List<Notification> list = new ArrayList<Notification>();
		ExecutorService executorSlaves = Executors.newCachedThreadPool();
		while(!notificationsQueue.isEmpty()) {

			if(notificationsQueue.peek().getLocalDateTime().equals(dateTime) || notificationsQueue.peek().getLocalDateTime().isBefore(dateTime)) { 
				list.add(notificationsQueue.poll());
			}
			else
				break;
		}

		System.out.println("list: "+list);

		for (int i = 0; i < list.size(); i++) {
			executorSlaves.execute(new ThreadSlave(list.get(i), userService, messagePushService,notificationService));
		}
		
		if(!notificationsQueue.isEmpty())
			createThread(notificationsQueue.peek().getLocalDateTime());
		else
			dateTime = null;

	};






}