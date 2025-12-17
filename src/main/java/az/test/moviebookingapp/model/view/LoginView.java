package az.test.moviebookingapp.model.view;

import az.test.moviebookingapp.entity.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public record LoginView(
        String jwtToken,
        String username,
        Set<Role> roles
) {
}
