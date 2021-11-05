package sv.com.udb.services.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.udb.services.commons.entities.Music;

public interface IMusicRepository extends JpaRepository<Music, Integer> {
}
