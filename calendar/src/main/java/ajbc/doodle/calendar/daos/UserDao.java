package ajbc.doodle.calendar.daos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;



@Transactional(rollbackFor = {DaoException.class}, readOnly = true)
public interface UserDao {

	//CRUD operations
	@Transactional(readOnly = false)
	public default void addUser(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default void addListUsers(List<User> users) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	
	@Transactional(readOnly = false)
	public default void updateUser(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default User getUser(Integer userId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	public default User getUserByEmail(String email) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default void deleteUser(Integer userId) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	//Queries
	
	// QUERIES
		public default List<User> getAllUser() throws DaoException {
			throw new DaoException("Method not implemented");
		}

		public default List<User> getUsersByTimeRange(LocalDateTime start, LocalDateTime end) throws DaoException {
			throw new DaoException("Method not implemented");
		}
		
		public default void deleteUserHard(User user) throws DaoException{
			throw new DaoException("Method not implemented");
		}
	
}
