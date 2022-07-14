package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"eventId", "event"})


@Entity
@Table(name = "Notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer notificationId;
	private Integer userId;
	private LocalDateTime localDateTime;
	private String title;
	@Column(name = "messageNot")
	private String message;
	@Enumerated(EnumType.STRING)
	private Unit unit;
	private int quantity;
	private int discontinued;
	@JsonIgnore
	private int isSend;
	
//	@JsonIgnore
	@Column(insertable=false, updatable=false)
	private Integer eventId;
	
	@JsonIgnore
//	@ManyToOne(cascade = {CascadeType.MERGE})
	@ManyToOne
	@JoinColumn(name="eventId")
	private Event event;

	
	public Notification(Integer userId, String title,String message, Unit unit, Integer quantity, Event event) {
		this.userId = userId;
		this.title = title;
		this.message = message;
		this.unit = unit;
		this.quantity = quantity;
		this.event = event;
		this.eventId = event.getEventId();
		if (unit.equals(Unit.HOURS))
			this.localDateTime = event.getStart().minusHours(quantity);
		else
			this.localDateTime = event.getStart().minusMinutes(quantity);
		
	}
	
	public void isSend(boolean bool) {
		if(bool)
			setIsSend(1);
		
		else
			setIsSend(0);
	}
}





