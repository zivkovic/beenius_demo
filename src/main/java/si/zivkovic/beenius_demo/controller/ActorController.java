package si.zivkovic.beenius_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.zivkovic.beenius_demo.model.Actor;
import si.zivkovic.beenius_demo.service.ActorService;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/actor")
public class ActorController {

	private static final int PAGING_SIZE = 10;

	@Autowired
	private ActorService actorService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity createActor(@RequestBody final Actor actor) {
		return ResponseEntity.ok(actorService.saveActor(actor));
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity getActor(@PathVariable @NotNull final long id) {
		if(id <= 0) {
			return ResponseEntity.badRequest().body("Id must be positive");
		}

		final Actor actor = actorService.getActor(id);

		if(actor == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(actor);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/all")
	public ResponseEntity getActors() {
		final List<Actor> actorList = actorService.getActors();

		if(actorList == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(actorList);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/all/{page}")
	public ResponseEntity getActorsWithPaging(@PathVariable(value = "page") int page) {
		final Page<Actor> actorList = actorService.getActorsWithPaging(new PageRequest(page, PAGING_SIZE));

		if(actorList == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(actorList);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity deleteActor(@PathVariable(value = "id") @NotNull final long id) {
		if(id <= 0) {
			return ResponseEntity.badRequest().body("Id must be positive");
		}

		final Actor actor = actorService.getActor(id);

		if(actor == null) {
			return ResponseEntity.notFound().build();
		}

		actorService.deleteActor(actor);

		return ResponseEntity.ok(actor);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity updateActor(@RequestBody @NotNull final Actor actor) {
		if(!actorService.actorExists(actor.getId())) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(actorService.saveActor(actor));
	}

}
