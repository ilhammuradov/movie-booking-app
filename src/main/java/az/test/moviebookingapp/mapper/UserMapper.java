package az.test.moviebookingapp.mapper;

import az.test.moviebookingapp.entity.User;
import az.test.moviebookingapp.model.view.UserView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roles")
    UserView toUserView(User user);
}
