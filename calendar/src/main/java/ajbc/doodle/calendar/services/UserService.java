package ajbc.doodle.calendar.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.entities.webpush.Subscription;
import lombok.Synchronized;


@Service
public class UserService {

	@Autowired
	@Qualifier("htUserDao")
	UserDao userDao;
	
	public void addUser(User user) throws DaoException{
		userDao.addUser(user);
	}
	
	public void addListUsers(List<User> users) throws DaoException{
		userDao.addListUsers(users);
	}
	
	public void updateUser(User user) throws DaoException {
		userDao.updateUser(user);
	}
	
	
	public synchronized User getUser(Integer userId) throws DaoException {
		return userDao.getUser(userId);
	}
	
	public User getUserByEmail(String email) throws DaoException {
		return userDao.getUserByEmail(email);
	}
	
	public List<User> getUsersByTimeRange(LocalDateTime start, LocalDateTime end) throws DaoException {
		return userDao.getUsersByTimeRange(start, end);
	}
	
	public void deleteUser(Integer userId) throws DaoException {
		userDao.deleteUser(userId);
	}
	
	public void deleteUserHard(User user) throws DaoException {
		userDao.deleteUserHard(user);
	}
	
	public List<User> getAllUser() throws DaoException{
		return userDao.getAllUser();
	}

	//log in 	
			public void login(User user, Subscription subscription) throws DaoException {
				user.loggIn(true);
				
				user.setEndPointLog(subscription.getEndpoint());
				user.setKeys(subscription.getKeys().getP256dh());
				user.setAuth(subscription.getKeys().getAuth());
				
				userDao.updateUser(user);
			}
			
			public void logout(User user) throws DaoException {
				user.loggIn(false);
				userDao.updateUser(user);
			}

	
}
