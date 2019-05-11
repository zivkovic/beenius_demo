package si.zivkovic.beenius_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import si.zivkovic.beenius_demo.model.Actor;
import si.zivkovic.beenius_demo.repository.ActorRepository;

import java.util.List;

@Service
public class ActorService {

	@Autowired
	private ActorRepository actorRepository;

	public Actor saveActor(final Actor actor) {
		// Do some business logic here, for now just return inserted movie
		return actorRepository.save(actor);
	}

	public Actor getActor(final long id) {
		// Do some business logic here, for now just return movie
		return actorRepository.findOne(id);
	}

	public List<Actor> getActors() {
		// Do some business logic here, for now just return movies
		return actorRepository.findAll();
	}

	public Page<Actor> getActorsWithPaging(final Pageable pageable) {
		// Do some business logic here, for now just return movies
		return actorRepository.findAll(pageable);
	}

	public void deleteActor(final Actor actor) {
		actorRepository.delete(actor);
	}

	public boolean actorExists(final long id) {
		return actorRepository.exists(id);
	}

}
