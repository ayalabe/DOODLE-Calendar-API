package ajbc.doodle.calendar.daos;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.Category;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Product;
import ajbc.doodle.calendar.entities.User;



@Transactional(rollbackFor = {DaoException.class}, readOnly = true)
public interface EventDao {

	//CRUD operations
	@Transactional(readOnly = false)
	public default void addEvent(Event even) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	@Transactional(readOnly = false)
	public default void updateEvent(Event even) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default Event getEvent(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	@Transactional(readOnly = false)
	public default void deleteEvent(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	//Queries
	
	// QUERIES
		public default List<Event> getAllEvent() throws DaoException {
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
