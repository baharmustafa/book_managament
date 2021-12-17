package project.masters.library.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

@Entity
@Table(name = "tbBooks")
public class BookEntity {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "Title", length = 100, nullable = false)
	private String title;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GenreID")
	private GenreEntity genre;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="AuthorID")
	private AuthorEntity author;

	
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public GenreEntity getGenre() {
		return genre;
	}



	public void setGenre(GenreEntity genre) {
		this.genre = genre;
	}



	public AuthorEntity getAuthor() {
		return author;
	}



	public void setAuthor(AuthorEntity author) {
		this.author = author;
	}



	@Transient
	public boolean hasTitleNameInList(List<String> titles) {
		for(String title : titles) {
			if(this.title.toLowerCase().contains(title.toLowerCase()))
				return true;
		}
		
		return false;
	}
}
