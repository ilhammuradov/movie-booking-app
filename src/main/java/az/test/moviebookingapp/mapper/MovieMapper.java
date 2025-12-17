package az.test.moviebookingapp.mapper;

import az.test.moviebookingapp.entity.Movie;
import az.test.moviebookingapp.model.request.MovieRequest;
import az.test.moviebookingapp.model.view.MovieView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring")
public interface MovieMapper {
    MovieView toMovieView(Movie movie);

    @Mapping(target = "shows", ignore = true)
    @Mapping(target = "id", ignore = true)
    Movie toMovie(MovieRequest movieRequest);

    List<MovieView> toMovieViewList(List<Movie> movies);
}
