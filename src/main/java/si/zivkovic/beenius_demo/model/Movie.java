package si.zivkovic.beenius_demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "imdbId"
)
@Entity
@Table(name = "MOVIE")
public class Movie {

	// Use imdbId instead of generatedId
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "imdbId", nullable = false)
	private String imdbId;

	@NotNull(message = "Movie title is required!")
	@NotEmpty(message = "Movie title must not be empty!")
	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description")
	private String description;

	@NotNull(message = "Movie year is required!")
	@Column(name = "year", nullable = false)
	private int year;

	@Lob
	@Column(name = "posterImage")
	private byte[] posterImage;

	@ManyToMany(
		fetch = FetchType.LAZY
	)
	@JoinTable(
		name = "MOVIE_ACTOR",
		joinColumns = @JoinColumn(name = "imdbId"),
		inverseJoinColumns = @JoinColumn(name = "id")
	)
	private List<Actor> actorList;

	@CreatedBy
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false)
	private Date created;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified", nullable = false)
	private Date modified;

	@PrePersist
	protected void onCreate() {
		this.created = new Date();
		this.modified = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.modified = new Date();
	}

	public Movie() {
		this.created = this.modified = new Date();
		this.actorList = new ArrayList<>();
	}

	public Movie(final String imdbId, final String title, final String description, final int year,
			final byte[] posterImage, final List<Actor> actorList) {
		this.imdbId = imdbId;
		this.title = title;
		this.description = description;
		this.year = year;
		this.posterImage = posterImage;
		this.actorList = actorList;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(final String imdbId) {
		this.imdbId = imdbId;
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

	public byte[] getPosterImage() {
		return posterImage;
	}

	public void setPosterImage(byte[] posterImage) {
		this.posterImage = posterImage;
	}

	public List<Actor> getActorList() {
		return actorList;
	}

	public void setActorList(List<Actor> actorList) {
		this.actorList = actorList;
	}

	public Date getCreated() {
		return created;
	}

	public Date getModified() {
		return modified;
	}

	public void addActor(final Actor actor) {
		this.actorList.add(actor);
	}

	public void removeActor(final Actor actor) {
		final Iterator<Actor> it = this.actorList.iterator();
		while(it.hasNext()) {
			final Actor currentActor = it.next();
			if(currentActor.getId() == actor.getId()) {
				it.remove();
				return;
			}
		}
	}

}
