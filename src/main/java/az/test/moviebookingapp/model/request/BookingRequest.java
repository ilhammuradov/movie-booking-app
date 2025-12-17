package az.test.moviebookingapp.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.List;

@Builder
public record BookingRequest(
        @Positive(message = "number of seats must be greater than zero")
        Integer numberOfSeats,
        @NotBlank
        List<String> seatNumbers,
        Long userId,
        Long showId
) {
}
