package ajbc.doodle.calendar.controllers;

import java.util.ArrayList;
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
/**
 * 
 * @author Ayala Maskalchi
 * 
 * Restful api service that receives http requests about notification of the calendar.
 *
 */
@RequestMapping("/notifications")
@RestController
public class NotificationController {

	@Autowired
	private NotificationService notificationServcie;
	@Autowired
	EventService eventService;
	@Autowired
	private MessagePushService messagePushService;
	@Autowired(required = false)
	private NotificationManager notificationManager;

	/**
	 * 
	 * @param notification
	 * @return new notification
	 */
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
	// Create Notification from list
//	@RequestMapping(method = RequestMethod.POST)
//	public ResponseEntity<?> addNotification(@RequestBody List<Notification> notifications) {
//		try {
//			List<Notification> addedNotifications = new ArrayList<Notification>();
//
//			for (int i = 0; i < notifications.size(); i++) {
//				notificationServcie.addNotification(notifications.get(i));
//
//				Notification notification = notificationServcie.getNotification(notifications.get(i).getNotificationId());
//				addedNotifications.add(notification);
//			}
//
//			return ResponseEntity.status(HttpStatus.CREATED).body(addedNotifications);
//		} catch (DaoException e) {
//			ErrorMessage errorMessage = new ErrorMessage();
//			errorMessage.setData(e.getMessage());
//			errorMessage.setMessage("failed to add notification to db");
//			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
//		}
//	}

	/**
	 * 
	 * @param id
	 * @return Notification by id
	 */
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

/**
 * push controller methods
 * 
 * @return
 */
	//push controller methods
	@GetMapping(path = "/publicSigningKey", produces = "application/octet-stream")
	public byte[] publicSigningKey() {
		return messagePushService.getServerKeys().getPublicKeyUncompressed();
	}

	@GetMapping(path = "/publicSigningKeyBase64")
	public String publicSigningKeyBase64() {
		return messagePushService.getServerKeys().getPublicKeyBase64();
	}

/**
 * 
 * @param map:
 * @param eventId Get notifications by event id 
 * @return Get all events
 * @throws DaoException
 */
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
	
	/**
	 * 
	 * @param notification
	 * @param id
	 * @return update Notification
	 */
	// Update Notification
		@RequestMapping(method = RequestMethod.PUT, path="/{id}")
		public ResponseEntity<?> updateProduct(@RequestBody Notification notification, @PathVariable Integer id) {

			try {
				notification.setNotificationId(id);
				notificationManager.removeFromQueue(notificationServcie.getNotification(notification.getNotificationId()));
				notificationServcie.updateNotification(notification);
				notificationManager.addQueue(notification);
				notification = notificationServcie.getNotification(notification.getNotificationId());
				return ResponseEntity.status(HttpStatus.OK).body(notification);
			} catch (DaoException e) {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setData(e.getMessage());
				errorMessage.setMessage("failed to update Notification in db");
				return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
			}
		}
		
		/**
		 * 
		 * @param notifications
		 * @return Update Notification from list notification
		 */
		@RequestMapping(method = RequestMethod.PUT)
		public ResponseEntity<?> updateListNotifications(@RequestBody List<Notification> notifications) {
			Notification not;
			List<Notification> addedNotifications = new ArrayList<Notification>();
			try {
				for (int i = 0; i < notifications.size(); i++) {
					notificationManager.removeFromQueue(notificationServcie.getNotification(notifications.get(i).getNotificationId()));
					notificationServcie.updateNotification(notifications.get(i));
					not= notificationServcie.getNotification(notifications.get(i).getNotificationId());
					notificationManager.addQueue(notifications.get(i));
					addedNotifications.add(not);
				}
				return ResponseEntity.status(HttpStatus.OK).body(addedNotifications);
			} catch (DaoException e) {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setData(e.getMessage());
				errorMessage.setMessage("failed to update user in db");
				return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
			}
		}
		
		
		
		
		
		/**
		 * 
		 * @param id
		 * @param map:
		 * @param soft delete soft notification
		 * @param hard delete hard notification 
		 * @return
		 */
		// Delete Notification
		@RequestMapping(method = RequestMethod.DELETE, path="/{id}")
		public ResponseEntity<?> deleteUser(@PathVariable Integer id, @RequestParam Map<String, String> map) {
			Set<String> keys = map.keySet();
			Notification notification = null;
			try {
				notification = notificationServcie.getNotification(id);
				if(keys.contains("soft")) {
					
					notificationServcie.deleteSoftNotification(id);
					notification = notificationServcie.getNotification(id);
					return ResponseEntity.status(HttpStatus.OK).body(notification);
					
				}
				if(keys.contains("hard")) {
					notificationServcie.deleteNotification(id);
					return ResponseEntity.status(HttpStatus.OK).body(notification);
				}


			} catch (DaoException e) {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setData(e.getMessage());
				errorMessage.setMessage("failed to delete notification from db");
				return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
			}
			return ResponseEntity.status(HttpStatus.OK).body(notification);

		}
		
		
//		@DeleteMapping
//		public ResponseEntity<List<Notification>> DeleteNotification(@RequestBody List<Integer> notificationIds, @RequestParam Map<String, String> map)
//				throws DaoException {
//			Set<String> keys = map.keySet();
//			List<Notification> notsList=new ArrayList<Notification>();
//
//			if (keys.contains("soft"))
//				for (Integer notId : notificationIds) {
//					notsList.add(notificationServcie.deleteSoftNotification(notId));
//				}
//
//			if (keys.contains("hard"))
//				for (Integer notId : notificationIds) {
//					notsList.add(notificationServcie.deleteNotification(notId));
//				}
//				
//			return ResponseEntity.ok(notsList);
//		}


}