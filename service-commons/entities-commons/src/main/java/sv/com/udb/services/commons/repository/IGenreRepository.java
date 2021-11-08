package sv.com.udb.services.commons.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.com.udb.services.commons.entities.Genre;

import java.util.Optional;

public interface IGenreRepository extends JpaRepository<Genre, Integer> {
   @EntityGraph(value = "music_by_genre",
                type = EntityGraph.EntityGraphType.LOAD)
   Optional<Genre> getAllById(Integer id);
}
