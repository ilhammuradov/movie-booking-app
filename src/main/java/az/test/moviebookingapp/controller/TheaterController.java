package az.test.moviebookingapp.controller;

import az.test.moviebookingapp.model.request.TheaterRequest;
import az.test.moviebookingapp.model.view.TheaterView;
import az.test.moviebookingapp.service.TheaterService;
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
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/theaters")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TheaterController {
    TheaterService theaterService;

    @GetMapping
    public ResponseEntity<List<TheaterView>> getAllTheaters() {
        return ResponseEntity.ok(theaterService.getAllTheaters());
    }

    @GetMapping("/location")
    public ResponseEntity<List<TheaterView>> getTheatersByLocation(@RequestParam("location") String location) {
        return ResponseEntity.ok(theaterService.getTheatersByLocation(location));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TheaterView> addTheater(@Valid @RequestBody TheaterRequest theater) {
        TheaterView created = theaterService.addTheater(theater);
        URI location = URI.create("/api/v1/theaters/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TheaterView> updateTheater(@PathVariable @NonNull Long id, @Valid @RequestBody TheaterRequest theater) {
        return ResponseEntity.ok(theaterService.updateTheater(id, theater));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.ok().build();
    }
}
