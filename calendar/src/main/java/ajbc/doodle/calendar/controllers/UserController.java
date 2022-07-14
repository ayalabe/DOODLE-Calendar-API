package ajbc.doodle.calendar.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.entities.webpush.Subscription;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.UserService;
import ajbc.doodle.calendar.entities.webpush.PushMessage;
import ajbc.doodle.calendar.entities.webpush.SubscriptionEndpoint;

import org.springframework.web.bind.annotation.PostMapping;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;




import com.fasterxml.jackson.core.JsonProcessingException;



@RequestMapping("/users")
@RestController
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	EventService eventService;
	@Autowired
	MessagePushService messagePushService;

	// Create users
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addUser(@RequestBody User user) {

		try {
			userService.addUser(user);
			user = userService.getUser(user.getUserId());
			return ResponseEntity.status(HttpStatus.CREATED).body(user);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add user to db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}
	
	// login
		@RequestMapping(method = RequestMethod.POST, path = "/login/{email}")
		public ResponseEntity<?> login(@RequestBody Subscription subscription, @PathVariable(required = false) String email)
				throws DaoException, InvalidKeyException, JsonProcessingException, NoSuchAlgorithmException,
				InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException,
				IllegalBlockSizeException, BadPaddingException {
			try {
				User user = userService.getUserByEmail(email);
				userService.login(user, subscription);
				messagePushService.sendPushMessage(user,
						messagePushService.encryptMessage(user, new PushMessage("message: ", "hello")));

				return ResponseEntity.ok().body("Logged in");
			} catch (DaoException e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
			}

		}

			@RequestMapping(method = RequestMethod.POST, path = "/logout/{email}")
			public ResponseEntity<?> logout(@PathVariable(required = false) String email) throws DaoException {
				try {
					User user = userService.getUserByEmail(email);
					userService.logout(user);
					return ResponseEntity.ok().body("Logged out");
				} catch (DaoException e) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
				}
			}
			
			@PostMapping("/isSubscribed")
			public boolean isSubscribed(@RequestBody SubscriptionEndpoint subscription) throws DaoException {
				List<User> users = userService.getAllUser();
				for(User user : users) {
					if(user.getEndPointLog() != null) {
						if(user.getEndPointLog().equals(subscription.getEndpoint()))
							return true;
					}
				}
				return false;
			}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getUser(@PathVariable Integer userId) {
		User user;
		try {
			user = userService.getUser(userId);
		} catch (DaoException e) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
			
	}
	
	
	
	// Get with param
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getUserssIds(@RequestParam Map<String, String> map) throws DaoException {

		Set<String> keys = map.keySet();
		User user = null;
		// Get all users of an event by event id
		if(keys.contains("eventId")) {
			Event event = 	eventService.getEvent(Integer.parseInt(map.get("eventId")));
			if (event == null)
				return ResponseEntity.notFound().build();
			Set<User> newList = event.getGuests();
			newList.add(userService.getUser(event.getOwnerId()));
			return ResponseEntity.ok(newList);
		}

		// Get user by email
		if (keys.contains("email")) {

			user = userService.getUserByEmail(map.get("email"));
			return ResponseEntity.ok(user);
		}

		// Get all user that have an event between start date and time to end date and time.
		if (keys.contains("start") && keys.contains("end")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
			LocalDateTime start = LocalDateTime.parse(map.get("start"), formatter);
			LocalDateTime end = LocalDateTime.parse(map.get("end"), formatter);
			List<User> usersList = userService.getUsersByTimeRange(start, end);
			return ResponseEntity.ok(usersList);
		}
		// Get all users
		List<User> list = userService.getAllUser();
		if (list == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(list);
	}


	@RequestMapping(method = RequestMethod.PUT, path="/{id}")
	public ResponseEntity<?> updateProduct(@RequestBody User user, @PathVariable Integer id) {

		try {
			user.setUserId(id);
			userService.updateUser(user);
			user = userService.getUser(user.getUserId());
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to update user in db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, path="/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id, @RequestParam Map<String, String> map) {
		Set<String> keys = map.keySet();
		User user = null;
		try {
			if(keys.contains("soft")) {
				userService.deleteUser(id);
				user = userService.getUser(id);
				
			}
			if(keys.contains("hard")) {
				userService.deleteUserHard(id);
				return ResponseEntity.status(HttpStatus.OK).body(user);
			}


		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to delete user from db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
		return ResponseEntity.status(HttpStatus.OK).body(user);

	}

}