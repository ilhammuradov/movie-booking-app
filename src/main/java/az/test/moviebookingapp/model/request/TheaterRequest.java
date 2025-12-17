package az.test.moviebookingapp.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record TheaterRequest(
        @NotBlank(message = "name must not be blank")
        String name,
        @NotBlank(message = "location must not be blank")
        String location,
        @Positive(message = "capacity should be higher than zero")
        Integer capacity,
        @NotBlank(message = "screen type must not be blank")
        String screenType
) {
}
