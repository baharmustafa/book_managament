package project.masters.library.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

@Entity
@Table(name = "tbAuthor")
public class AuthorEntity {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "Name", length = 100, nullable = false)
	private String name;
	
	@Column(name = "lastName", length = 1024)
	private String lname;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	@Transient
	public boolean hasIdInList(List<String> ids) {
		for(String id : ids) {
			if(Integer.parseInt(id) == this.id)
				return true;
		}
		
		return false;
	}
	
}
