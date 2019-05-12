package si.zivkovic.beenius_demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import si.zivkovic.beenius_demo.model.Actor;
import si.zivkovic.beenius_demo.model.Movie;
import si.zivkovic.beenius_demo.repository.ActorRepository;
import si.zivkovic.beenius_demo.repository.MovieRepository;
import si.zivkovic.beenius_demo.service.MovieService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

	@Autowired
	private MovieService movieService;

	@MockBean
	private MovieRepository movieRepository;

	@MockBean
	private ActorRepository actorRepository;

	@Test
	public void testCreate() {
		final Movie movie = new Movie(
				"tt5884052", "Pokémon Detective Pikachu", "Pokemon.",
				2019, null, new ArrayList<>()
		);

		given(movieRepository.save(movie)).willReturn(movie);

		assertThat(movieService.saveMovie(movie)).isEqualToComparingOnlyGivenFields(
				movie, "imdbId", "title", "description", "year"
		);
	}

	@Test
	public void testSearchOnMovies() {
		final Movie movie = new Movie(
				"tt5884052", "Pokémon Detective Pikachu", "Pokemon.",
				2019, null, new ArrayList<>()
		);
		final Movie movie1 = new Movie(
				"tt58840521", "Pokémon Reynolds Pikachu1", "Pokemon.1",
				2019, null, new ArrayList<>()
		);
		final Movie movie2 = new Movie(
				"tt58840522", "Ryan Pikachu2", "Pokemon.2",
				2019, null, new ArrayList<>()
		);

		given(movieRepository.save(movie)).willReturn(movie);
		given(movieRepository.save(movie1)).willReturn(movie1);
		given(movieRepository.save(movie2)).willReturn(movie2);
		given(movieRepository.findAll()).willReturn(Arrays.asList(movie, movie1, movie2));
		given(movieRepository.findMoviesBySearchString("%detective%")).willReturn(Collections.singletonList(movie));
		given(movieRepository.findMoviesBySearchString("%pokémon%")).willReturn(Arrays.asList(movie, movie1));
		given(movieRepository.findMoviesBySearchString("%pokemon%")).willReturn(new ArrayList<>());
		given(movieRepository.findMoviesBySearchString("%nothing%")).willReturn(new ArrayList<>());

		assertThat(movieService.findMoviesBySearchString("detective")).hasSize(1);
		assertThat(movieService.findMoviesBySearchString("pokémon")).hasSize(2);
		assertThat(movieService.findMoviesBySearchString("pokemon")).hasSize(0);
		assertThat(movieService.findMoviesBySearchString("nothing")).hasSize(0);
	}

	@Test
	public void testAddingActorToMovie() {
		final Movie movie = new Movie(
				"tt5884052", "Pokémon Detective Pikachu", "Pokemon.",
				2019, null, new ArrayList<>()
		);
		final Actor actor = new Actor(
				"Janez", "Novak", new Date()
		);

		given(movieRepository.findOne(movie.getImdbId())).willReturn(movie);
		given(actorRepository.findOne(actor.getId())).willReturn(actor);
		given(movieRepository.save(movie)).willReturn(movie);
		given(actorRepository.save(actor)).willReturn(actor);

		final Movie movieWithActor = movieService.addActorToMovie(movie.getImdbId(), actor.getId());
		given(movieRepository.findOne(movieWithActor.getImdbId())).willReturn(movieWithActor);

		assertThat(movieService.getMovie(movieWithActor.getImdbId()).getActorList()).hasSize(1);

		final Movie movieWithoutActor = movieService.removeActorFromMovie(movieWithActor.getImdbId(), actor.getId());
		given(movieRepository.findOne(movieWithoutActor.getImdbId())).willReturn(movieWithoutActor);

		assertThat(movieService.getMovie(movieWithoutActor.getImdbId()).getActorList()).hasSize(0);
	}

}
