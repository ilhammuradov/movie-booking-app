package az.test.moviebookingapp.mapper;

import az.test.moviebookingapp.entity.Show;
import az.test.moviebookingapp.model.request.ShowRequest;
import az.test.moviebookingapp.model.view.ShowView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShowMapper {

    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "theater.id", target = "theaterId")
    ShowView toShowView(Show show);

    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "theater", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    Show toShow(ShowRequest showRequest);

    List<ShowView> toShowViewList(List<Show> shows);
}
