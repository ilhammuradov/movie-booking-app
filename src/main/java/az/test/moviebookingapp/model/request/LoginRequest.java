package az.test.moviebookingapp.model.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "User name cannot be empty!")
        String username,

        @NotBlank(message = "Password cannot be empty!")
        String password
) {
}
