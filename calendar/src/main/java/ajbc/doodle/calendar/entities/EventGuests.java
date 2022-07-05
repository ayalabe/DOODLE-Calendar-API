package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter


@Entity
@Table(name = "eventGuests")
public class EventGuests {
	
	private Integer eventId;
	private Integer categoryId;

}
