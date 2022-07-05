package ajbc.doodle.calendar.daos;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.Category;
import ajbc.doodle.calendar.entities.Product;
import ajbc.doodle.calendar.entities.User;



@Transactional(rollbackFor = {DaoException.class}, readOnly = true)
public interface UserDao {

	//CRUD operations
	@Transactional(readOnly = false)
	public default void addUser(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	@Transactional(readOnly = false)
	public default void updateUser(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default User getUser(Integer userId) throws DaoException {
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

		public default List<Product> getProductsByPriceRange(Double min, Double max) throws DaoException {
			throw new DaoException("Method not implemented");
		}

		public default List<Product> getProductsInCategory(Integer categoryId) throws DaoException {
			throw new DaoException("Method not implemented");
		}

		public default List<Product> getProductsNotInStock() throws DaoException {
			throw new DaoException("Method not implemented");
		}

		public default List<Product> getProductsOnOrder() throws DaoException {
			throw new DaoException("Method not implemented");
		}

		public default List<Product> getDiscontinuedProducts() throws DaoException {
			throw new DaoException("Method not implemented");
		}
		
		public default Category getCategoryByProdactId() throws DaoException {
			throw new DaoException("Method not implemented");
		}

		public default long count() throws DaoException {
			throw new DaoException("Method not implemented");
		}
	
	
}
