package az.test.moviebookingapp.repository;

import az.test.moviebookingapp.entity.Booking;
import az.test.moviebookingapp.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long id);

    List<Booking> findByShowId(Long id);

    List<Booking> findByBookingStatus(BookingStatus status);
}
