package az.test.moviebookingapp.model.view;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ShowView(
        Long id,
        LocalDateTime showTime,
        Double price,
        Long movieId,
        Long theaterId
) {
}
