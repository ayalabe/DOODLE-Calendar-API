package ajbc.doodle.calendar.manager;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ajbc.doodle.calendar.entities.Notification;

public class NotificationManager {
	
	
	public static Queue<Notification> notificationsQueue = new PriorityQueue<Notification>(new Comparator<Notification>() {
		@Override
		public int compare(Notification n1, Notification n2) {
			
			if(n1.getLocalDateTime().equals(n2.getLocalDateTime()))
				return 0;
			
			else if(n1.getLocalDateTime().isAfter(n2.getLocalDateTime()))
				return -1;
			
			return 1;
		}});
	
	public static void addQueue(Notification notification) {
		notificationsQueue.add(notification);
	}
	
//
//	public static void main(String[] args) throws InterruptedException, ExecutionException {
//		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
//		executorService.schedule(() -> System.out.println(4), 5, TimeUnit.SECONDS);
//	}
	
	
	
}