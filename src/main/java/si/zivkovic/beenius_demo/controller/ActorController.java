package si.zivkovic.beenius_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.zivkovic.beenius_demo.model.Actor;
import si.zivkovic.beenius_demo.service.ActorService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/actor")
public class ActorController {

	private static final int PAGING_SIZE = 10;
	private static final CacheControl cacheControl = CacheControl.maxAge(5, TimeUnit.MINUTES);

	@Autowired
	private ActorService actorService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity createActor(@RequestBody final Actor actor) {
		return ResponseEntity.ok(actorService.saveActor(actor));
	}

	@Cacheable(cacheNames = "getActorCache", key = "#id")
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity getActor(@PathVariable @NotNull final long id) {
		if(id <= 0) {
			return ResponseEntity.badRequest().body("Id must be positive.");
		}

		final Actor actor = actorService.getActor(id);

		if(actor == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().cacheControl(cacheControl).body(actor);
	}

	@Cacheable(cacheNames = "getActorsCache")
	@RequestMapping(method = RequestMethod.GET, path = "/all")
	public ResponseEntity getActors() {
		final List<Actor> actorList = actorService.getActors();

		if(actorList == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().cacheControl(cacheControl).body(actorList);

	}

	@Cacheable(cacheNames = "getActorsWithPagingCache", key = "#page")
	@RequestMapping(method = RequestMethod.GET, path = "/all/{page}")
	public ResponseEntity getActorsWithPaging(@PathVariable(value = "page") int page) {
		if(page < 0) {
			return ResponseEntity.badRequest().body("Page must be positive.");
		}
		final Page<Actor> actorList = actorService.getActorsWithPaging(new PageRequest(page, PAGING_SIZE));

		if(actorList == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().cacheControl(cacheControl).body(actorList);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity deleteActor(@PathVariable(value = "id") @NotNull final long id) {
		if(id <= 0) {
			return ResponseEntity.badRequest().body("Id must be positive.");
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

		return ResponseEntity.ok(actorService.updateActor(actor));
	}

}
