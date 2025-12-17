package az.test.moviebookingapp.model.view;

import az.test.moviebookingapp.entity.BookingStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BookingView(
        Long id,
        Integer numberOfSeats,
        LocalDateTime bookingTime,
        Double price,
        BookingStatus bookingStatus,
        List<String> seatNumbers,
        Long userId,
        Long showId
) {
}

