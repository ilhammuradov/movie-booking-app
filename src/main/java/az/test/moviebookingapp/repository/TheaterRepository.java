package az.test.moviebookingapp.repository;

import az.test.moviebookingapp.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {

    List<Theater> findTheatersByLocation(String location);
}
