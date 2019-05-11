package si.zivkovic.beenius_demo.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.zivkovic.beenius_demo.model.Movie;
import si.zivkovic.beenius_demo.service.ActorService;
import si.zivkovic.beenius_demo.service.MovieService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/movie")
public class MovieController {

	private static final int PAGING_SIZE = 10;
	private static final CacheControl cacheControl = CacheControl.maxAge(5, TimeUnit.MINUTES);

	@Autowired
	private MovieService movieService;

	@Autowired
	private ActorService actorService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity createMovie(@RequestBody final Movie movie) {
		return ResponseEntity.ok(movieService.saveMovie(movie));
	}

	@Cacheable(cacheNames = "getMovieCache", key = "#id")
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity getMovie(@PathVariable @NotNull final String id) {
		Movie movie = movieService.getMovie(id);

		if(movie == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().cacheControl(cacheControl).body(movie);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/{movieId}/actor/{actorId}")
	public ResponseEntity addActorToMovie(@PathVariable("movieId") final String movieId,
			@PathVariable("actorId") final long actorId) {
		if(!movieService.movieExists(movieId)) {
			return ResponseEntity.badRequest().body("Movie for id '" + movieId + "' not found.");
		}

		if(!actorService.actorExists(actorId)) {
			return ResponseEntity.badRequest().body("Actor for id '" + actorId + "' not found.");
		}

		return ResponseEntity.ok(movieService.addActorToMovie(movieId, actorId));
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/{movieId}/actor/{actorId}")
	public ResponseEntity removeActorFromMovie(@PathVariable("movieId") final String movieId,
										  @PathVariable("actorId") final long actorId) {
		if(!movieService.movieExists(movieId)) {
			return ResponseEntity.badRequest().body("Movie for id '" + movieId + "' not found.");
		}

		if(!actorService.actorExists(actorId)) {
			return ResponseEntity.badRequest().body("Actor for id '" + actorId + "' not found.");
		}

		return ResponseEntity.ok(movieService.removeActorFromMovie(movieId, actorId));
	}

	@Cacheable(cacheNames = "getMoviesCache")
	@RequestMapping(method = RequestMethod.GET, path = "/all")
	public ResponseEntity getMovies() {
		final List<Movie> movieList = movieService.getMovies();

		if(movieList == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().cacheControl(cacheControl).body(movieList);

	}

	@Cacheable(cacheNames = "getMoviesWithPagingCache", key = "#page")
	@RequestMapping(method = RequestMethod.GET, path = "/all/{page}")
	public ResponseEntity getMoviesWithPaging(@PathVariable(value = "page") int page) {
		if(page <= 0) {
			return ResponseEntity.badRequest().body("Page must be positive.");
		}

		final Page<Movie> movieList = movieService.getMoviesWithPaging(new PageRequest(page, PAGING_SIZE));

		if(movieList == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().cacheControl(cacheControl).body(movieList);
	}

	@Cacheable(cacheNames = "searchMoviesCache", key = "#searchString")
	@RequestMapping(method = RequestMethod.GET, path = "/all/search")
	public ResponseEntity searchMovies(@RequestParam("search") @NotNull final String searchString) {
		if(StringUtils.isBlank(searchString)) {
			return ResponseEntity.badRequest().body("Parameter 'search' is missing or empty!");
		}

		final List<Movie> movieList = movieService.findMoviesBySearchString(searchString);

		return ResponseEntity.ok().cacheControl(cacheControl).body(movieList);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity deleteMovie(@PathVariable(value = "id") @NotNull final String id) {
		if(StringUtils.isBlank(id)) {
			return ResponseEntity.badRequest().body("Parameter 'id' is missing or empty!");
		}

		final Movie movie = movieService.getMovie(id);

		if(movie == null) {
			return ResponseEntity.notFound().build();
		}

		movieService.deleteMovie(movie);

		return ResponseEntity.ok(movie);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity updateMovie(@RequestBody @NotNull final Movie movie) {
		if(!movieService.movieExists(movie.getImdbId())) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(movieService.saveMovie(movie));
	}

}
