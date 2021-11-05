package sv.com.udb.services.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.udb.services.commons.entities.Genre;

import java.util.Optional;

public interface IGenreRepository extends JpaRepository<Genre, Integer> {
   Optional<Genre> getByTitle(String title);
}
