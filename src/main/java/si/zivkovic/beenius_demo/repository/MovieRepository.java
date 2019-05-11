package si.zivkovic.beenius_demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.zivkovic.beenius_demo.model.Movie;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

	Page<Movie> findAll(final Pageable pageable);

}
