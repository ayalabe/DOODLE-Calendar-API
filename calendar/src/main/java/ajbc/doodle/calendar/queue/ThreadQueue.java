package ajbc.doodle.calendar.queue;

import java.util.Comparator;
import java.util.PriorityQueue;

import ajbc.doodle.calendar.entities.Notification;

public class ThreadQueue {

	 //Comparing by employee names
	static Comparator<Notification> dateTimeSorter = Comparator.comparing(Notification::getLocalDateTime);
	 
	public static PriorityQueue<Notification> priorityQueue = new PriorityQueue<>( dateTimeSorter );
}
