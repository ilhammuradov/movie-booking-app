package az.test.moviebookingapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "movies")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true, nullable = false)
    String title;
    String description;
    String genre;
    Integer duration;
    LocalDate releaseDate;
    String language;
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    List<Show> shows;
}
