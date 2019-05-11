package si.zivkovic.beenius_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.zivkovic.beenius_demo.model.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

}
