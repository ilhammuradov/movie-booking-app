package az.test.moviebookingapp.controller;

import az.test.moviebookingapp.model.request.MovieRequest;
import az.test.moviebookingapp.model.view.MovieView;
import az.test.moviebookingapp.service.MovieService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/movies")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieController {
    MovieService movieService;

    @GetMapping
    public ResponseEntity<List<MovieView>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/genre")
    public ResponseEntity<List<MovieView>> getMoviesByGenre(@RequestParam String genre) {
        return ResponseEntity.ok(movieService.getMoviesByGenre(genre));
    }

    @GetMapping("/language")
    public ResponseEntity<List<MovieView>> getMoviesByLanguage(@RequestParam String language) {
        return ResponseEntity.ok(movieService.getMoviesByLanguage(language));
    }

    @GetMapping("/title")
    public ResponseEntity<MovieView> getMovieByTitle(@RequestParam String title) {
        return ResponseEntity.ok(movieService.getMovieByTitle(title));
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieView> addMovie(@RequestBody MovieRequest movieRequest) {
        MovieView createdMovie = movieService.addMovie(movieRequest);
        URI location = URI.create("/api/v1/movies/" + createdMovie.id());
        return ResponseEntity.created(location).body(createdMovie);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieView> updateMovie(@PathVariable @NonNull Long id, @RequestBody MovieRequest movieRequest) {
        return ResponseEntity.ok(movieService.updateMovie(id,movieRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable @NonNull Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok().build();
    }
}
