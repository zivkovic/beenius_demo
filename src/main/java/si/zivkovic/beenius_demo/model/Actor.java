package si.zivkovic.beenius_demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
		property = "id"
)
@Entity
@Table(name = "ACTOR")
public class Actor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private long id;

	@NotNull(message = "First name is required!")
	@NotEmpty(message = "First name must not be empty!")
	@Column(name = "firstName", nullable = false)
	private String firstName;

	@NotNull(message = "Last name is required!")
	@NotEmpty(message = "Last name must not be empty!")
	@Column(name = "lastName", nullable = false)
	private String lastName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateOfBirth", nullable = false)
	@NotNull(message = "Date of birth is required!")
	private Date dateOfBirth;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "actorList")
	private List<Movie> movieList;

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

	public Actor() {}

	public Actor(String firstName, String lastName, Date dateOfBirth, final List<Movie> movieList) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.movieList = movieList;
	}

	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public List<Movie> getMovieList() {
		if(this.movieList == null) {
			return new ArrayList<>();
		}
		return movieList;
	}

	public void setMovieList(List<Movie> movieList) {
		this.movieList = movieList;
	}

	public Date getCreated() {
		return created;
	}

	public Date getModified() {
		return modified;
	}

	public void addMovie(final Movie movie) {
		getMovieList().add(movie);
	}

	public void removeMovie(final Movie movie) {
		final Iterator<Movie> it = getMovieList().iterator();
		while(it.hasNext()) {
			final Movie currentMovie = it.next();
			if(currentMovie.getImdbId().equals(movie.getImdbId())) {
				it.remove();
				return;
			}
		}
	}

	public void update(final Actor fromActor) {
		if(getId() != fromActor.getId()) {
			return;
		}
		if(StringUtils.isNotBlank(fromActor.getFirstName())) {
			this.firstName = fromActor.getFirstName();
		}
		if(StringUtils.isNotBlank(fromActor.getLastName())) {
			this.lastName = fromActor.getLastName();
		}
		if(fromActor.getDateOfBirth() != null) {
			this.dateOfBirth = fromActor.getDateOfBirth();
		}
		if(CollectionUtils.isNotEmpty(fromActor.getMovieList())) {
			this.movieList = fromActor.getMovieList();
		}
	}

}
