package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "logInDats")
public class LogInData {
	private String endPoint;
	private String keys;
	private String auth;

}
