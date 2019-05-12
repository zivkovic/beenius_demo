package si.zivkovic.beenius_demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import si.zivkovic.beenius_demo.model.Movie;
import si.zivkovic.beenius_demo.repository.MovieRepository;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MovieRepositoryTest {

	@Autowired
	private MovieRepository movieRepository;

	@Test
	public void testInsertionFetchingAndDeletion() {
		final Movie movie = new Movie(
				"tt5884052", "Pokémon Detective Pikachu", "Pokemon.",
				2019, null, new ArrayList<>()
		);

		assertThat(movieRepository.save(movie)).isNotNull();
		assertThat(movieRepository.findOne(movie.getImdbId())).isEqualToComparingOnlyGivenFields(
				movie, "imdbId", "title", "description", "year"
		);
		movieRepository.delete(movie.getImdbId());
		assertThat(movieRepository.findOne(movie.getImdbId())).isNull();
	}

	@Test
	public void testMultiInsertionFetchingAndDeletion() {
		final Movie movie = new Movie(
				"tt5884052", "Pokémon Detective Pikachu", "Pokemon.",
				2019, null, new ArrayList<>()
		);
		final Movie movie1 = new Movie(
				"tt58840521", "Pokémon Detective Pikachu1", "Pokemon.1",
				2019, null, new ArrayList<>()
		);

		assertThat(movieRepository.save(movie)).isNotNull();
		assertThat(movieRepository.save(movie1)).isNotNull();
		assertThat(movieRepository.findAll()).hasSize(2);

		movieRepository.delete(movie.getImdbId());
		assertThat(movieRepository.findAll()).hasSize(1);
		movieRepository.delete(movie1.getImdbId());
		assertThat(movieRepository.findAll()).hasSize(0);
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

		assertThat(movieRepository.save(movie)).isNotNull();
		assertThat(movieRepository.save(movie1)).isNotNull();
		assertThat(movieRepository.save(movie2)).isNotNull();
		assertThat(movieRepository.findAll()).hasSize(3);

		assertThat(movieRepository.findMoviesBySearchString("%detective%")).hasSize(1);
		assertThat(movieRepository.findMoviesBySearchString("%pokémon%")).hasSize(2);
		assertThat(movieRepository.findMoviesBySearchString("%pokemon%")).hasSize(0);
		assertThat(movieRepository.findMoviesBySearchString("%nothing%")).hasSize(0);
	}

	@Test
	public void testPaging() {
		final int size = 13;
		for(int i = 0; i < size; i++) {
			final Movie movie = new Movie(
					"tt588405" + i, "Pokémon Detective Pikachu", "Pokemon.",
					2019, null, new ArrayList<>()
			);
			assertThat(movieRepository.save(movie)).isNotNull();
		}
		assertThat(movieRepository.findAll()).hasSize(size);

		assertThat(movieRepository.findAll(new PageRequest(0, 10))).hasSize(10);
		assertThat(movieRepository.findAll(new PageRequest(1, 10))).hasSize(3);
		assertThat(movieRepository.findAll(new PageRequest(0, 20))).hasSize(13);
	}

}
