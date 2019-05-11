package si.zivkovic.beenius_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import si.zivkovic.beenius_demo.model.Movie;
import si.zivkovic.beenius_demo.repository.MovieRepository;

import java.util.List;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;

	public Movie saveMovie(final Movie movie) {
		// Do some business logic here, for now just return inserted movie
		return movieRepository.save(movie);
	}

	public Movie getMovie(final long id) {
		// Do some business logic here, for now just return movie
		return movieRepository.findOne(id);
	}

	public List<Movie> getMovies() {
		// Do some business logic here, for now just return movies
		return movieRepository.findAll();
	}

	public Page<Movie> getMoviesWithPaging(final Pageable pageable) {
		// Do some business logic here, for now just return movies
		return movieRepository.findAll(pageable);
	}

	public List<Movie> findMoviesBySearchString(String searchString) {
		// Do some business logic here, for now just return movies filtered by searchString
		// Prepend and append % to prevent using CONCAT multiple times in query. Use lowercase here for the same reason.
		searchString = "%" + searchString.toLowerCase() + "%";
		return movieRepository.findMoviesBySearchString(searchString);
	}

	public void deleteMovie(final Movie movie) {
		movieRepository.delete(movie);
	}

	public boolean movieExists(final long id) {
		return movieRepository.exists(id);
	}

}
