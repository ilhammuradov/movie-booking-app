package az.test.moviebookingapp.service;

import az.test.moviebookingapp.entity.Theater;
import az.test.moviebookingapp.exception.TheaterNotFoundException;
import az.test.moviebookingapp.mapper.TheaterMapper;
import az.test.moviebookingapp.model.request.TheaterRequest;
import az.test.moviebookingapp.model.view.TheaterView;
import az.test.moviebookingapp.repository.TheaterRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TheaterService {
    TheaterRepository theaterRepository;
    TheaterMapper theaterMapper;

    public List<TheaterView> getAllTheaters() {
        List<Theater> theaters = theaterRepository.findAll();
        return theaterMapper.toTheaterViewList(theaters);
    }

    public List<TheaterView> getTheatersByLocation(String location) {
        List<Theater> theaterList = theaterRepository.findTheatersByLocation(location);
        if (theaterList.isEmpty()) {
            throw new TheaterNotFoundException();
        }
        return theaterMapper.toTheaterViewList(theaterList);
    }

    public TheaterView addTheater(TheaterRequest theaterRequest) {
        Theater theater = theaterMapper.toTheater(theaterRequest);
        return theaterMapper.toTheaterView(theaterRepository.save(theater));
    }

    public TheaterView updateTheater(Long id,TheaterRequest theaterRequest) {
        return theaterRepository.findById(id).map(theater -> {
            theater.setName(theaterRequest.name());
            theater.setLocation(theaterRequest.location());
            theater.setScreenType(theaterRequest.screenType());
            theater.setCapacity(theaterRequest.capacity());
            return theaterMapper.toTheaterView(theaterRepository.save(theater));
        }).orElseThrow(TheaterNotFoundException::new);
    }

    public void deleteTheater(Long id) {
        if(!theaterRepository.existsById(id)) {
            throw new TheaterNotFoundException();
        }
        theaterRepository.deleteById(id);
    }
}
