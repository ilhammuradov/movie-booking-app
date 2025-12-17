package az.test.moviebookingapp.service;

import az.test.moviebookingapp.entity.Movie;
import az.test.moviebookingapp.exception.MovieNotFoundException;
import az.test.moviebookingapp.mapper.MovieMapper;
import az.test.moviebookingapp.model.request.MovieRequest;
import az.test.moviebookingapp.model.view.MovieView;
import az.test.moviebookingapp.repository.MovieRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieService {
    MovieRepository movieRepository;
    MovieMapper movieMapper;

    public List<MovieView> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movieMapper.toMovieViewList(movies);
    }

    public List<MovieView> getMoviesByGenre(String genre) {
        List<Movie> movies = movieRepository.findByGenre(genre);
        if (movies.isEmpty()) {
            throw new MovieNotFoundException();
        }
        return movieMapper.toMovieViewList(movies);
    }


    public List<MovieView> getMoviesByLanguage(String language) {
        List<Movie> movies = movieRepository.findByLanguage(language);
        if (movies.isEmpty()) {
            throw new MovieNotFoundException();
        }
        return movieMapper.toMovieViewList(movies);
    }

    public MovieView getMovieByTitle(String title) {
        return movieRepository.findByTitle(title)
                .map(movieMapper::toMovieView)
                .orElseThrow(MovieNotFoundException::new);
    }

    public MovieView addMovie(MovieRequest movieRequest) {
        Movie movie = movieMapper.toMovie(movieRequest);
        return movieMapper.toMovieView(movieRepository.save(movie));
    }

    public MovieView updateMovie(Long id, MovieRequest movieRequest) {
        return movieRepository.findById(id).map(movie -> {
            movie.setTitle(movieRequest.title());
            movie.setGenre(movieRequest.genre());
            movie.setLanguage(movieRequest.language());
            movie.setDuration(movieRequest.duration());
            movie.setDescription(movieRequest.description());
            movie.setReleaseDate(movieRequest.releaseDate());
            return movieMapper.toMovieView(movieRepository.save(movie));
        }).orElseThrow(MovieNotFoundException::new);
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new MovieNotFoundException();
        }
        movieRepository.deleteById(id);
    }
}
