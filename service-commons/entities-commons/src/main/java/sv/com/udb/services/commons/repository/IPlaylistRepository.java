package sv.com.udb.services.commons.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.com.udb.services.commons.entities.Playlist;

import java.util.List;
import java.util.Optional;

public interface IPlaylistRepository extends JpaRepository<Playlist, Integer> {
   @EntityGraph(value = "playlist_song",
                type = EntityGraph.EntityGraphType.LOAD)
   @Query("SELECT p from playlist p WHERE p.user.id = ?1")
   List<Playlist> findByUserIdWithSongs(String id);

   @EntityGraph(value = "playlist_song",
                type = EntityGraph.EntityGraphType.LOAD)
   @Query("SELECT p from playlist p WHERE p.id = ?1")
   Optional<Playlist> findByIdWithSongs(Integer id);

   @EntityGraph(value = "playlist_song",
                type = EntityGraph.EntityGraphType.LOAD)
   @Query("SELECT p from playlist p")
   List<Playlist> findAllWithSong();
}
