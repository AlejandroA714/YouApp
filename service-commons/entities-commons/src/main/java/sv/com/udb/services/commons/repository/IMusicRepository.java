package sv.com.udb.services.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.com.udb.services.commons.entities.Music;

import java.util.List;

public interface IMusicRepository extends JpaRepository<Music, Integer> {
   @Query("SELECT m from music m WHERE m.title LIKE CONCAT('%',?1,'%') AND m.status.id = 4")
   List<Music> findAllByTitleContains(String title);

   @Override
   @Query("SELECT m from music m WHERE m.status.id = 4")
   List<Music> findAll();
}
