package az.test.moviebookingapp.repository;

import az.test.moviebookingapp.entity.Show;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByMovieId(Long id);

    List<Show> findByTheaterId(Long id);

//    @Query("SELECT s FROM Show s LEFT JOIN FETCH s.bookings WHERE s.id = :id")
// for lazy initialization exception
//    Optional<Show> findByIdWithBookings(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Show s LEFT JOIN FETCH s.bookings WHERE s.id = :id")
    Optional<Show> findByIdWithLock(@Param("id") Long id);

    List<Show> findByShowTimeAfterOrderByShowTimeAsc(LocalDateTime from);

    boolean existsByTheaterIdAndShowTime(Long theaterId, LocalDateTime showTime);

    boolean existsByTheaterIdAndShowTimeAndIdNot(Long theaterId, LocalDateTime showTime, Long id);
}


