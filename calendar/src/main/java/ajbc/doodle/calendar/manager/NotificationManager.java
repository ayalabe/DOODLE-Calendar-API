package ajbc.doodle.calendar.manager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.UserService;

@Service
public class NotificationManager {

	@Autowired
	UserService userService;
	@Autowired
	MessagePushService messagePushService;

	private static final int NUM_THREAD_M = 1;
	private long nextTime = 5, timeOther;

	ScheduledExecutorService executorManager = Executors.newScheduledThreadPool(NUM_THREAD_M);
	public static Queue<Notification> notificationsQueue = new PriorityQueue<Notification>(new Comparator<Notification>() {
		@Override
		public int compare(Notification n1, Notification n2) {

			if(n1.getLocalDateTime().equals(n2.getLocalDateTime()))
				return 0;

			else if(n1.getLocalDateTime().isAfter(n2.getLocalDateTime()))
				return 1;

			return -1;
		}});

	public void addQueue(Notification notification) throws DaoException, InterruptedException {
		nextTime = Duration.between(LocalDateTime.now(), notification.getLocalDateTime()).getSeconds();

		if(notificationsQueue.isEmpty()) {
			System.out.println("nextTime: "+nextTime);
			notificationsQueue.add(notification);
			executorManager.schedule(managerQueue, nextTime, TimeUnit.SECONDS);
		}

		else 
			if(notification.getLocalDateTime().isBefore(notificationsQueue.peek().getLocalDateTime())) {
				executorManager.shutdown();
				executorManager.awaitTermination(2, TimeUnit.SECONDS);
				notificationsQueue.add(notification);
				ScheduledExecutorService executorManager = Executors.newScheduledThreadPool(NUM_THREAD_M);
				executorManager.schedule(managerQueue, nextTime, TimeUnit.SECONDS);

			}



	}


	public Runnable managerQueue = () -> {
		List<Notification> list = new ArrayList<Notification>();
		ExecutorService executorSlaves = Executors.newCachedThreadPool();
		LocalDateTime dateTime = null;
		boolean firstTime = true;
		while(!notificationsQueue.isEmpty()) {
			if(firstTime) { 
				list.add(notificationsQueue.poll());
				dateTime = list.get(0).getLocalDateTime();
				System.out.println("dateTime: "+dateTime);
				firstTime = false;
			}

			else
				if(notificationsQueue.peek().getLocalDateTime().equals(dateTime)) {
					list.add(notificationsQueue.poll());
				}
				else
					break;
		}

		System.out.println("list: "+list);

		for (int i = 0; i < list.size(); i++) {
			executorSlaves.execute(new ThreadSlave(list.get(i), userService, messagePushService));
		}

	};




}