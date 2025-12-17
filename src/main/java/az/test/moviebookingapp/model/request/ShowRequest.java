package az.test.moviebookingapp.model.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ShowRequest(
        @FutureOrPresent
        LocalDateTime showTime,
        @Positive
        Double price,
        @NotNull
        Long movieId,
        @NotNull
        Long theaterId
) {
}
