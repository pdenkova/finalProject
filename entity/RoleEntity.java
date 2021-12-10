package uni.fin.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import uni.fin.data.AbstractEntity;

@Entity
public class RoleEntity extends AbstractEntity{
	
 	
 	@Column(name = "code", unique = true, nullable = false)
 	private String code;
 	
 	@Column(name = "description")
 	private String description;
 	
 	public RoleEntity() {
 		
 	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

