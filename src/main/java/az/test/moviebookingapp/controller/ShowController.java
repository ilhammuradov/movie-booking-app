package az.test.moviebookingapp.controller;

import az.test.moviebookingapp.model.request.ShowRequest;
import az.test.moviebookingapp.model.view.ShowView;
import az.test.moviebookingapp.service.ShowService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/shows")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowController {
    ShowService showService;

    @GetMapping
    public ResponseEntity<List<ShowView>> getAllShows() {
        return ResponseEntity.ok(showService.getAllShows());
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowView>> getAllShowsByMovie(@PathVariable @NonNull Long movieId) {
        return ResponseEntity.ok(showService.getAllShowsByMovie(movieId));
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<ShowView>> getAllShowsByTheater(@PathVariable @NonNull Long theaterId) {
        return ResponseEntity.ok(showService.getAllShowsByTheater(theaterId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<ShowView>> getUpcomingShows(@RequestParam Optional<LocalDateTime> from) {
        return ResponseEntity.ok(showService.getUpcomingShows(from.orElse(LocalDateTime.now())));
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowView> createShow(@Valid @RequestBody ShowRequest show) {
        ShowView created = showService.createShow(show);
        URI location = URI.create("/api/v1/shows/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowView> updateShow(@PathVariable @NonNull Long id, @Valid @RequestBody ShowRequest show) {
        return ResponseEntity.ok(showService.updateShow(id,show));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteShow(@PathVariable @NonNull Long id) {
        showService.deleteShow(id);
        return ResponseEntity.noContent().build();
    }
}
