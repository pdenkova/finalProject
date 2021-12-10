package uni.fin.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class NotificationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private UserEntity fromUser;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private UserEntity toUser;
	
	@Column(name = "comment", length = 150)
	private String comment;
	
	@Column(name = "status", length = 25)
	private String status;
	
	private NotificationEntity() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

	public UserEntity getFromUser() {
		return fromUser;
	}

	public void setFromUser(UserEntity fromUser) {
		this.fromUser = fromUser;
	}

	public UserEntity getToUser() {
		return toUser;
	}

	public void setToUser(UserEntity toUser) {
		this.toUser = toUser;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
