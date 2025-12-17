package az.test.moviebookingapp.mapper;

import az.test.moviebookingapp.entity.Theater;
import az.test.moviebookingapp.model.request.TheaterRequest;
import az.test.moviebookingapp.model.view.TheaterView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TheaterMapper {
    TheaterView toTheaterView(Theater theater);

    @Mapping(target = "shows", ignore = true)
    @Mapping(target = "id", ignore = true)
    Theater toTheater(TheaterRequest theaterRequest);

    List<TheaterView> toTheaterViewList(List<Theater> theaters);
}
