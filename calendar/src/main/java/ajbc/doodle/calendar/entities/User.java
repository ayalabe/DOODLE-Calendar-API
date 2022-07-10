package ajbc.doodle.calendar.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
@Setter


@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	
	private String firstName;
	private String lastName;
	
	private String email;
	
	private LocalDate birthdate;
	private LocalDate joinDate;
	private Integer discontinued;
	private Integer isLogin;
	
//	@JsonIgnore
	@JsonBackReference
//	@ManyToMany(mappedBy="guests", cascade = {CascadeType.MERGE})
	@ManyToMany(mappedBy="guests", cascade = {CascadeType.MERGE}) 
	@Fetch(FetchMode.JOIN)
	Set<Event> events = new HashSet<Event>();
	
	

}
