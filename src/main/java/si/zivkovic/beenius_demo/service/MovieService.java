package si.zivkovic.beenius_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import si.zivkovic.beenius_demo.model.Movie;
import si.zivkovic.beenius_demo.repository.MovieRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;

	public Movie insertMovie(final Movie movie) {
		// Do some business logic here, for now just return actor
		return movieRepository.save(movie);
	}

	public Movie getMovie(final long id) {
		// Do some business logic here, for now just return actor
		return movieRepository.findOne(id);
	}

	public List<Movie> getMovies() {
		return movieRepository.findAll();
	}

	public Page<Movie> getMoviesWithPaging(final Pageable pageable) {
		return movieRepository.findAll(pageable);
	}

}
