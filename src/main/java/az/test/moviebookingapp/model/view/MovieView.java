package az.test.moviebookingapp.model.view;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MovieView(
        Long id,
        String title,
        String description,
        String genre,
        Integer duration,
        LocalDate releaseDate,
        String language
) {
}
