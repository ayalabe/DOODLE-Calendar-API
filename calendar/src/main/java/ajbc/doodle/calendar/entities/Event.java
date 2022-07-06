package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
@Setter


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
	
	@ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable(name = "EventGuests", joinColumns = @JoinColumn(name = "eventId"), inverseJoinColumns = @JoinColumn(name = "userId"))
	private List<User> guests;
	
//	@ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
//	@JoinTable(name = "Users_Events", joinColumns = @JoinColumn(name = "eventId"), inverseJoinColumns = @Join
//	private Map<K, List<No>> notification;
	
	
	
	

}
