package az.test.moviebookingapp.service;

import az.test.moviebookingapp.entity.Booking;
import az.test.moviebookingapp.entity.BookingStatus;
import az.test.moviebookingapp.entity.Show;
import az.test.moviebookingapp.entity.User;
import az.test.moviebookingapp.exception.*;
import az.test.moviebookingapp.mapper.BookingMapper;
import az.test.moviebookingapp.model.request.BookingRequest;
import az.test.moviebookingapp.model.view.BookingView;
import az.test.moviebookingapp.repository.BookingRepository;
import az.test.moviebookingapp.repository.ShowRepository;
import az.test.moviebookingapp.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingService {
    BookingRepository bookingRepository;
    ShowRepository showRepository;
    UserRepository userRepository;
    BookingMapper bookingMapper;

    public List<BookingView> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookingMapper.toBookingViewList(bookings);
    }

    public List<BookingView> getUserBookings(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        if (bookings.isEmpty()) {
            throw new BookingNotFoundException();
        }
        return bookingMapper.toBookingViewList(bookings);
    }

    public List<BookingView> getShowBookings(Long showId) {
        List<Booking> bookings = bookingRepository.findByShowId(showId);
        if (bookings.isEmpty()) {
            throw new BookingNotFoundException();
        }
        return bookingMapper.toBookingViewList(bookings);
    }

    public List<BookingView> getBookingsByStatus(BookingStatus status) {
        List<Booking> bookings = bookingRepository.findByBookingStatus(status);
        if (bookings.isEmpty()) {
            throw new BookingNotFoundException();
        }
        return bookingMapper.toBookingViewList(bookings);
    }

    @Transactional
    public BookingView createBooking(BookingRequest bookingRequest) {
        Show show = showRepository.findByIdWithLock(bookingRequest.showId())
                .orElseThrow(() -> new ShowNotFoundException("Show not found with ID: " + bookingRequest.showId()));

        if (!isSeatsAvailable(show, bookingRequest.numberOfSeats())) {
            throw new NotEnoughSeatsException();
        }

        if (bookingRequest.seatNumbers().size() != bookingRequest.numberOfSeats()) {
            throw new BookingConflictException("Size of seats should be equal to number of seats");
        }

        validateDuplicateSeats(show, bookingRequest.seatNumbers());

        User user = userRepository.findById(bookingRequest.userId())
                .orElseThrow(UserNotFoundException::new);

        Booking booking = bookingMapper.toBooking(bookingRequest);
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookingTime(LocalDateTime.now());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPrice(calculateTotalAmount(show.getPrice(), bookingRequest.numberOfSeats()));

        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toBookingView(saved);
    }

    public BookingView confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(BookingNotFoundException::new);

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new BookingConflictException("Only pending bookings can be confirmed.");
        }

        // Payment

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toBookingView(saved);
    }


    public BookingView cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(BookingNotFoundException::new);

        validateCancellation(booking);

        booking.setBookingStatus(BookingStatus.CANCELLED);
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toBookingView(saved);
    }

    private void validateCancellation(Booking booking) {
        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new BookingConflictException("Only confirmed bookings can be cancelled.");
        }

        if (booking.getShow().getShowTime().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BookingConflictException("Cancellations are only allowed at least 2 hours before the show.");
        }
    }

    private Double calculateTotalAmount(Double price, Integer numberOfSeats) {
        return price * numberOfSeats;
    }

    private void validateDuplicateSeats(Show show, List<String> seatNumbers) {
        Set<String> alreadyBookedSeats = show.getBookings().stream()
                .filter(booking -> booking.getBookingStatus() != BookingStatus.CANCELLED)
                .flatMap(booking -> booking.getSeatNumbers().stream())
                .collect(Collectors.toSet());

        List<String> duplicateSeats = seatNumbers.stream()
                .filter(alreadyBookedSeats::contains)
                .toList();

        if (!duplicateSeats.isEmpty()) {
            throw new BookingConflictException("These seats are already booked: " + duplicateSeats);
        }
    }

    public boolean isSeatsAvailable(Show show, Integer seatsRequested) {
        int bookedSeats = show.getBookings().stream()
                .filter(booking -> booking.getBookingStatus() != BookingStatus.CANCELLED)
                .mapToInt(Booking::getNumberOfSeats)
                .sum();

        int availableSeats = show.getTheater().getCapacity() - bookedSeats;
        return availableSeats >= seatsRequested;
    }
}
