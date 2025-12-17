package az.test.moviebookingapp.model.view;

import lombok.Builder;

@Builder
public record TheaterView(
        Long id,
        String name,
        String location,
        Integer capacity,
        String screenType
) {
}
