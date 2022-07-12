package ajbc.doodle.calendar.controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.manager.NotificationManager;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.NotificationService;

@RequestMapping("/notifications")
@RestController
public class NotificationController {

	@Autowired
	private NotificationService notificationServcie;
	@Autowired
	EventService eventService;
	@Autowired
	private MessagePushService messagePushService;

	// Create Notification
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNotification(@RequestBody Notification notification) {

		try {
			notificationServcie.addNotification(notification);
			notification = notificationServcie.getNotification(notification.getNotificationId());
			return ResponseEntity.status(HttpStatus.CREATED).body(notification);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add Notification to db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	// Get Notification by id
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getEventById(@PathVariable Integer id) {

		Notification notification;
		try {
			notification = notificationServcie.getNotification(id);
			return ResponseEntity.ok(notification);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to get notification with id " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
		}
	}


	//push controller methods
	@GetMapping(path = "/publicSigningKey", produces = "application/octet-stream")
	public byte[] publicSigningKey() {
		return messagePushService.getServerKeys().getPublicKeyUncompressed();
	}

	@GetMapping(path = "/publicSigningKeyBase64")
	public String publicSigningKeyBase64() {
		return messagePushService.getServerKeys().getPublicKeyBase64();
	}


	// Get with param
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getUserssIds(@RequestParam Map<String, String> map) throws DaoException {

		Set<String> keys = map.keySet();

		// Get notifications by event id
		if (keys.contains("eventId")) {
			Event event = eventService.getEvent(Integer.parseInt(map.get("eventId")));
			return ResponseEntity.ok(event.getNotifications());
		}

		// Get all events
		List<Notification> notificationsList = notificationServcie.getAllNotification();
		if (notificationsList == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(notificationsList);
	}
	
	// Update Notification
		@RequestMapping(method = RequestMethod.PUT, path="/{id}")
		public ResponseEntity<?> updateProduct(@RequestBody Notification notification, @PathVariable Integer id) {

			try {
				notification.setNotificationId(id);
				notificationServcie.updateNotification(notification);
				notification = notificationServcie.getNotification(notification.getNotificationId());
				return ResponseEntity.status(HttpStatus.OK).body(notification);
			} catch (DaoException e) {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setData(e.getMessage());
				errorMessage.setMessage("failed to update Notification in db");
				return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
			}
		}


}