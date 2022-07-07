package ajbc.doodle.calendar.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.UserService;


@RequestMapping("/users")
@RestController
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	EventService eventService;

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

	//	// Get with param
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getUserssIds(@RequestParam Map<String, String> map) throws DaoException {

		Set<String> keys = map.keySet();
		User user = null;
		// Get all users of an event by event id
		if(keys.contains("eventId")) {

			Event event = 	eventService.getEvent(Integer.parseInt(map.get("eventId")));
			if (event == null)
				return ResponseEntity.notFound().build();
			List<User> newList = event.getGuests();
			newList.add(userService.getUser(event.getOwnerId()) );
			return ResponseEntity.ok(newList);
		}

		// Get user by id
		if(keys.contains("userId")) {
			user = userService.getUser(Integer.parseInt(map.get("userId")));
			if (user == null)
				return ResponseEntity.notFound().build();
			return ResponseEntity.ok(user);
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
				user = userService.getUser(id);
				userService.deleteUser(id);
				user = userService.getUser(id);
				
			}
			if(keys.contains("hard")) {
				userService.deleteUserHard(userService.getUser(id));
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


	//	@RequestMapping(method = RequestMethod.POST)
	//	public ResponseEntity<?> addUsers(@RequestParam List<User> users) {
	//		System.out.println("lllll");
	//		try {
	//			userService.addListUsers(users);
	//			return ResponseEntity.status(HttpStatus.CREATED).body(users);
	//		} catch (DaoException e) {
	//			ErrorMessage errorMessage = new ErrorMessage();
	//			errorMessage.setData(e.getMessage());
	//			errorMessage.setMessage("failed to add user to db");
	//			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
	//		}
	//	}
	//	




	//	@RequestMapping(method = RequestMethod.GET)
	//	public ResponseEntity<User> getProductsByEmail(@RequestParam Optional<String> email) throws DaoException {
	//		User userReturn = null;
	//		if (email.isPresent())
	//			userReturn = userService.getUserByEmail(email.get());
	////		else
	////			List<User> list = userService.getAllUser();
	//		
	//		if (userReturn == null)
	//			return ResponseEntity.notFound().build();
	//
	//		return ResponseEntity.ok(userReturn);
	//	}

	//	@RequestMapping(method = RequestMethod.GET)
	//	public ResponseEntity<List<Product>> getProductsByRange(@RequestParam Map<String, String> map) throws DaoException {
	//		List<Product> list;
	//		Set<String> keys = map.keySet();
	//		
	//		if (keys.contains("min") && keys.contains("max"))
	//			list = dao.getProductsByPriceRange(Double.parseDouble(map.get("min")), Double.parseDouble(map.get("max")));
	//		else
	//			list = dao.getAllProducts();
	//		
	//		if (list == null)
	//			return ResponseEntity.notFound().build();
	//
	//		return ResponseEntity.ok(list);
	//	}
	//	
	//	@RequestMapping(method = RequestMethod.GET, path="/{id}")
	//	public ResponseEntity<?> getProductsById(@PathVariable Integer id) {
	//		
	//		Product prod;
	//		try {
	//			prod = dao.getProduct(id);
	//			return ResponseEntity.ok(prod);
	//		} catch (DaoException e) {
	//			ErrorMessage errorMessage = new ErrorMessage();
	//			errorMessage.setData(e.getMessage());
	//			errorMessage.setMessage("failed to get product with id "+id);
	//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
	//		}
	//	}
	//	
	//	@RequestMapping(method = RequestMethod.POST)
	//	public ResponseEntity<?> addProduct(@RequestBody Product prod) {
	//		
	//		try {
	//			dao.addProduct(prod);
	//			prod = dao.getProduct(prod.getProductId());
	//			return ResponseEntity.status(HttpStatus.CREATED).body(prod);
	//		} catch (DaoException e) {
	//			ErrorMessage errorMessage = new ErrorMessage();
	//			errorMessage.setData(e.getMessage());
	//			errorMessage.setMessage("failed to add product to db");
	//			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
	//		}
	//	}
	//	
	//	@RequestMapping(method = RequestMethod.PUT, path="/{id}")
	//	public ResponseEntity<?> updateProduct(@RequestBody Product prod, @PathVariable Integer id) {
	//		
	//		try {
	//			prod.setProductId(id);
	//			dao.updateProduct(prod);
	//			prod = dao.getProduct(prod.getProductId());
	//			return ResponseEntity.status(HttpStatus.OK).body(prod);
	//		} catch (DaoException e) {
	//			ErrorMessage errorMessage = new ErrorMessage();
	//			errorMessage.setData(e.getMessage());
	//			errorMessage.setMessage("failed to update product in db");
	//			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
	//		}
	//	}
	//	
	//
	//	@RequestMapping(method = RequestMethod.DELETE, path="/{id}")
	//	public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
	//		
	//		try {
	//			Product prod = dao.getProduct(id);
	//			dao.deleteProduct(id);
	//			prod = dao.getProduct(id);
	//			return ResponseEntity.status(HttpStatus.OK).body(prod);
	//		} catch (DaoException e) {
	//			ErrorMessage errorMessage = new ErrorMessage();
	//			errorMessage.setData(e.getMessage());
	//			errorMessage.setMessage("failed to delete product from db");
	//			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
	//		}
	//	}
}