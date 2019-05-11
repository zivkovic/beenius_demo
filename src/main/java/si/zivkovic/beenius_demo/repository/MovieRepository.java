package si.zivkovic.beenius_demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import si.zivkovic.beenius_demo.model.Movie;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

	Page<Movie> findAll(final Pageable pageable);

	// Simple search using only title field.
	// If searching is required on other fields as well, use bottom commented function and query.
	@Query("SELECT m FROM Movie m WHERE lower(title) like :searchString")
	List<Movie> findMoviesBySearchString(@Param("searchString") final String searchString);

	/*
	@Query("SELECT m FROM Movie m WHERE lower(title) like :searchString" +
			" OR lower(description) like :searchString" +
			" OR year like :searchString")
	List<Movie> findMoviesBySearchStringOnAllFields(@Param("searchString") final String searchString);
	 */

}
