package si.zivkovic.beenius_demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import si.zivkovic.beenius_demo.model.Actor;
import si.zivkovic.beenius_demo.repository.ActorRepository;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ActorRepositoryTest {

	@Autowired
	private ActorRepository actorRepository;

	@Test
	public void testInsertionFetchingAndDeletion() {
		final Actor actor = new Actor(
				"Janez", "Novak", new Date()
		);

		assertThat(actorRepository.save(actor)).isNotNull();
		assertThat(actorRepository.findOne(actor.getId())).isEqualToComparingOnlyGivenFields(
				actor, "firstName", "lastName", "dateOfBirth"
		);
		actorRepository.delete(actor.getId());
		assertThat(actorRepository.findOne(actor.getId())).isNull();
	}

	@Test
	public void testMultiInsertionFetchingAndDeletion() {
		final Actor actor = new Actor(
				"Janez", "Novak", new Date()
		);
		final Actor actor1 = new Actor(
				"Jana", "Novak", new Date()
		);

		assertThat(actorRepository.save(actor)).isNotNull();
		assertThat(actorRepository.save(actor1)).isNotNull();
		assertThat(actorRepository.findAll()).hasSize(2);

		actorRepository.delete(actor.getId());
		assertThat(actorRepository.findAll()).hasSize(1);
		actorRepository.delete(actor1.getId());
		assertThat(actorRepository.findAll()).hasSize(0);
	}

	@Test
	public void testPaging() {
		final int size = 13;
		for(int i = 0; i < size; i++) {
			final Actor actor = new Actor(
					"Janez" + i, "Novak", new Date()
			);
			assertThat(actorRepository.save(actor)).isNotNull();
		}
		assertThat(actorRepository.findAll()).hasSize(size);

		assertThat(actorRepository.findAll(new PageRequest(0, 10))).hasSize(10);
		assertThat(actorRepository.findAll(new PageRequest(1, 10))).hasSize(3);
		assertThat(actorRepository.findAll(new PageRequest(0, 20))).hasSize(13);
	}

}
