package ajbc.doodle.calendar.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.manager.NotificationManager;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.NotificationService;

@RequestMapping("/notifications")
@RestController
public class NotificationController {
	
	@Autowired
	private NotificationService notificationServcie;
	
	
	@Autowired
	private MessagePushService messagePushService;
	
	@Autowired(required = false)
	private NotificationManager notificationManager;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNotification(@RequestBody Notification notification, @RequestParam int userId ,@RequestParam Integer eventId) {
		try {
			notificationServcie.addNotification(notification);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(e.getMessage());
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllNotifications() {
		try {
			List<Notification> notifications = notificationServcie.getAllNotification();
			return ResponseEntity.status(HttpStatus.CREATED).body(notifications);
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(e.getMessage());
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

	
}