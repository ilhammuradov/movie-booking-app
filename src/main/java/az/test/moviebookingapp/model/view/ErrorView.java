package az.test.moviebookingapp.model.view;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ErrorView(
        Integer errorCode,
        String errorMessage,
        List<String> errors,
        LocalDateTime timestamp
) {}
