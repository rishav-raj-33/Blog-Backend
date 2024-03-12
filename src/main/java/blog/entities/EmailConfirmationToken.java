package blog.entities;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//E mail confirmation token in the database

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EmailConfirmationToken {
	public EmailConfirmationToken(String token, LocalDateTime createAt, LocalDateTime expires) {
		super();
		this.token = token;
		this.createAt = createAt;
		this.expires = expires;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	private String token;
	private LocalDateTime createAt;
	private LocalDateTime expires;
	private LocalDateTime confirmedAt;
	
	@ManyToOne
	private User user;
}
