package az.test.moviebookingapp.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "User name cannot be empty!")
        String username,

        @NotBlank(message = "Email cannot be empty!")
        @Email(message = "Valid email is required!")
        String email,

        @NotBlank(message = "Password cannot be empty!")
        @Size(min = 8, message = "Password should be at least 8 characters long!")
        String password
) {}

