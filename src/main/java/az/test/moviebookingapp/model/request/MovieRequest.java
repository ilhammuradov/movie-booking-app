package az.test.moviebookingapp.model.request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MovieRequest(
        String title,
        String description,
        String genre,
        Integer duration,
        LocalDate releaseDate,
        String language
) {
}
