package az.test.moviebookingapp.controller;

import az.test.moviebookingapp.entity.BookingStatus;
import az.test.moviebookingapp.model.request.BookingRequest;
import az.test.moviebookingapp.model.view.BookingView;
import az.test.moviebookingapp.service.BookingService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    BookingService bookingService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingView>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingView>> getUserBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<BookingView>> getShowBookings(@PathVariable Long showId) {
        return ResponseEntity.ok(bookingService.getShowBookings(showId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookingView>> getBookingsByStatus(@PathVariable BookingStatus status) {
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status));
    }

    @PostMapping
    public ResponseEntity<BookingView> createBooking(@RequestBody @Valid BookingRequest booking) {
        BookingView created = bookingService.createBooking(booking);
        URI location = URI.create("api/v1/bookings/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<BookingView> confirmBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingView> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}
