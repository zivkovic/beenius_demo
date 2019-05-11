package si.zivkovic.beenius_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.zivkovic.beenius_demo.model.Movie;
import si.zivkovic.beenius_demo.service.MovieService;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

	private static final int PAGING_SIZE = 10;

	@Autowired
	private MovieService movieService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Movie> insertMovie(@RequestBody Movie movie) {
		return ResponseEntity.ok(movieService.insertMovie(movie));
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<Movie> getMovie(@PathVariable @NotNull final long id) {
		Movie movie = movieService.getMovie(id);

		if(movie == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(movie);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/all")
	public ResponseEntity<List<Movie>> getMovies() {
		final List<Movie> movieList = movieService.getMovies();

		if(movieList == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(movieList);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/all/{page}")
	public ResponseEntity<Page<Movie>> getMoviesWithPaging(@PathVariable(value = "page") int page) {
		final Page<Movie> movieList = movieService.getMoviesWithPaging(new PageRequest(page, PAGING_SIZE));

		if(movieList == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(movieList);
	}



}
