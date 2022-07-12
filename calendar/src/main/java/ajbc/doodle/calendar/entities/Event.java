package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import javax.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor

@Entity
@Table(name = "events")
public class Event {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eventId;
	private Integer ownerId;
	
	private String title;
	private Integer isAllDay;
	@Column(name = "startDateTime")
	private LocalDateTime start;
	@Column(name = "endDateTime")
	private LocalDateTime end;
	@Column(name = "eAddress")
	private String address;
	@Column(name = "EventDescription")
	private String description;
	private Integer discontinued;
	@Enumerated(EnumType.STRING)
	private RepeatingOptions repeating;
	
//	@ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE,CascadeType.REFRESH})
//	@JoinTable(name = "EventGuests", joinColumns = @JoinColumn(name = "eventId"), inverseJoinColumns = @JoinColumn(name = "userId"))
//	@JsonManagedReference
//	@JsonProperty(access = Access.AUTO)
	@ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE,CascadeType.REFRESH, CascadeType.REMOVE})
	@JoinTable(name = "EventGuests", joinColumns = @JoinColumn(name = "eventId"), inverseJoinColumns = @JoinColumn(name = "userId"))
	private Set<User> guests = new HashSet<User>();
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name="eventId")
//	@JsonManagedReference
	private Set<Notification> notifications = new HashSet<Notification>();
	
	
	
	

}
