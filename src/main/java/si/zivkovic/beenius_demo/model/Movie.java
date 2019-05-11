package si.zivkovic.beenius_demo.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "movie")
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "title", nullable = false)
	@NotNull(message = "Movie title is required!")
	@NotEmpty(message = "Movie title must not be empty!")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "year", nullable = false)
	@NotNull(message = "Movie year is required!")
	private int year;

	//private List<Actor> actorList;

	@CreatedBy
	@Temporal(TemporalType.DATE)
	@Column(name = "created", nullable = false)
	private Date created;

	@LastModifiedDate
	@Temporal(TemporalType.DATE)
	@Column(name = "modified", nullable = false)
	private Date modified;

	// TODO images...

	public Movie() {}

	public Movie(final String title, final String description, final int year) {// final List<Actor> actorList
		this.title = title;
		this.description = description;
		this.year = year;
		//this.actorList = actorList;
	}

	@PrePersist
	protected void onCreate() {
		this.created = new Date();
		this.modified = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.modified = new Date();
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Date getCreated() {
		return created;
	}

	public Date getModified() {
		return modified;
	}

	/*
	public List<Actor> getActorList() {
		return actorList;
	}

	public void setActorList(List<Actor> actorList) {
		this.actorList = actorList;
	}

	public void addActor(final Actor actor) {
		if(this.actorList == null) {
			this.actorList = new ArrayList<Actor>();
		}
		this.actorList.add(actor);
	}
	 */

}
