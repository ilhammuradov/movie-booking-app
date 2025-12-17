package az.test.moviebookingapp.mapper;

import az.test.moviebookingapp.entity.Booking;
import az.test.moviebookingapp.model.request.BookingRequest;
import az.test.moviebookingapp.model.view.BookingView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "show.id", target = "showId")
    BookingView toBookingView(Booking booking);

    List<BookingView> toBookingViewList(List<Booking> bookings);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "show", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookingTime", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    Booking toBooking(BookingRequest request);
}

