package az.test.moviebookingapp.service;

import az.test.moviebookingapp.entity.Movie;
import az.test.moviebookingapp.entity.Show;
import az.test.moviebookingapp.entity.Theater;
import az.test.moviebookingapp.exception.MovieNotFoundException;
import az.test.moviebookingapp.exception.ShowConflictException;
import az.test.moviebookingapp.exception.ShowNotFoundException;
import az.test.moviebookingapp.exception.TheaterNotFoundException;
import az.test.moviebookingapp.mapper.ShowMapper;
import az.test.moviebookingapp.model.request.ShowRequest;
import az.test.moviebookingapp.model.view.ShowView;
import az.test.moviebookingapp.repository.MovieRepository;
import az.test.moviebookingapp.repository.ShowRepository;
import az.test.moviebookingapp.repository.TheaterRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowService {
    ShowRepository showRepository;
    MovieRepository movieRepository;
    TheaterRepository theaterRepository;
    ShowMapper showMapper;

    public List<ShowView> getAllShows() {
        List<Show> shows = showRepository.findAll();
        return showMapper.toShowViewList(shows);
    }

    public List<ShowView> getAllShowsByMovie(Long id) {
        List<Show> shows = showRepository.findByMovieId(id);
        if (shows.isEmpty()) {
            throw new ShowNotFoundException("No show found for this movie");
        }
        return showMapper.toShowViewList(shows);
    }

    public List<ShowView> getAllShowsByTheater(Long id) {
        List<Show> shows = showRepository.findByTheaterId(id);
        if (shows.isEmpty()) {
            throw new ShowNotFoundException("No show found for this theater");
        }
        return showMapper.toShowViewList(shows);
    }

    public List<ShowView> getUpcomingShows(LocalDateTime from) {
        List<Show> shows = showRepository.findByShowTimeAfterOrderByShowTimeAsc(from);
        if (shows.isEmpty()) {
            throw new ShowNotFoundException("No show found for this time");
        }
        return showMapper.toShowViewList(shows);
    }

    public ShowView createShow(ShowRequest request) {
        duplicateCheck(request.theaterId(), request.showTime());

        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(MovieNotFoundException::new);

        Theater theater = theaterRepository.findById(request.theaterId())
                .orElseThrow(TheaterNotFoundException::new);

        Show show = showMapper.toShow(request);
        show.setMovie(movie);
        show.setTheater(theater);

        Show created = showRepository.save(show);
        return showMapper.toShowView(created);
    }

    public ShowView updateShow(Long id, ShowRequest request) {
        conflictCheck(request.theaterId(), request.showTime(), id);

        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ShowNotFoundException("Show not found with ID: " + id));

        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(MovieNotFoundException::new);

        Theater theater = theaterRepository.findById(request.theaterId())
                .orElseThrow(TheaterNotFoundException::new);

        show.setShowTime(request.showTime());
        show.setPrice(request.price());
        show.setMovie(movie);
        show.setTheater(theater);

        Show updated = showRepository.save(show);
        return showMapper.toShowView(updated);
    }

    private void duplicateCheck(Long theaterId, LocalDateTime showTime) {
        boolean duplicateCheck = showRepository.existsByTheaterIdAndShowTime(theaterId, showTime);
        if (duplicateCheck) {
            throw new ShowConflictException("A show already exists at this time in the selected theater.");
        }
    }

    private void conflictCheck(Long theaterId, LocalDateTime showTime, Long id) {
        boolean conflict = showRepository.existsByTheaterIdAndShowTimeAndIdNot(
                theaterId, showTime, id);

        if (conflict) {
            throw new ShowConflictException("Another show already exists at this time in the selected theater.");
        }
    }

    public void deleteShow(Long id) {
        if (!showRepository.existsById(id)) {
            throw new ShowNotFoundException("Show not found with ID: " + id);
        }
        showRepository.deleteById(id);
    }
}
