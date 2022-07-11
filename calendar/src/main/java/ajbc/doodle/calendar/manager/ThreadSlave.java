package ajbc.doodle.calendar.manager;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;

import ajbc.doodle.calendar.controllers.PushController;
import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.entities.webpush.PushMessage;
import ajbc.doodle.calendar.services.UserService;

public class ThreadSlave implements Runnable {
	
	private UserService userService;
	private PushController pushController;
	private Notification notification;
	
	
	public ThreadSlave(Notification notification, UserService userService, PushController pushController) {
		this.notification = notification;
		this.userService = userService;
		this.pushController = pushController;
		System.out.println("in ThreadSlave");
	}

	@Override
	public void run() {
		try {

			User user = userService.getUser(notification.getUserId());
			if(user.getIsLogin() == 0)
				return;
			System.out.println("in run");
			pushController.sendPushMessageToSubscribers(user.getKeys(), user.getAuth(), user.getEndPointLog(), new PushMessage("message: ", notification.toString()));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
