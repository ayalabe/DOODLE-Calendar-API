package ajbc.doodle.calendar.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.User;


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

	public User getUser(Integer userId) throws DaoException {
		return userDao.getUser(userId);
	}
	
	public User getUserByEmail(String email) throws DaoException {
		return userDao.getUserByEmail(email);
	}
	
	public void deleteUser(Integer userId) throws DaoException {
		userDao.deleteUser(userId);
	}
	
	public List<User> getAllUser() throws DaoException{
		return userDao.getAllUser();
	}

	
}
