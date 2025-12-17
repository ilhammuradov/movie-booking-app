package az.test.moviebookingapp.model.view;

import az.test.moviebookingapp.entity.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserView(
        Long id,
        String username,
        String email,
        Set<Role> roles
) {
}
