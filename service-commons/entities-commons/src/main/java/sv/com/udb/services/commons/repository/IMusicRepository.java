package sv.com.udb.services.commons.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.com.udb.services.commons.entities.Music;

import javax.persistence.NamedEntityGraph;
import java.util.List;
import java.util.Optional;

public interface IMusicRepository extends JpaRepository<Music, Integer> {
   @EntityGraph(value = "music_favorites",
                type = EntityGraph.EntityGraphType.LOAD)
   @Query("SELECT m from music m WHERE m.title LIKE CONCAT('%',?1,'%') AND m.status.id = 4")
   List<Music> findAllByTitleContains(String title);

   @Override
   @EntityGraph(value = "music_favorites",
                type = EntityGraph.EntityGraphType.LOAD)
   @Query("SELECT m from music m WHERE m.status.id = 4")
   List<Music> findAll();

   @Override
   @EntityGraph(value = "music_favorites",
                type = EntityGraph.EntityGraphType.LOAD)
   @Query("SELECT m from music m WHERE m.id = ?1 AND m.status.id = 4")
   Optional<Music> findById(Integer id);

   @EntityGraph(value = "music_favorites",
       type = EntityGraph.EntityGraphType.LOAD)
   @Query("SELECT m from music m WHERE m.id = ?1")
   Optional<Music> findByIdU(Integer id);
}
