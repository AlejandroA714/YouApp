package sv.com.udb.youapp.services.storage.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.com.udb.services.commons.entities.Genre;
import sv.com.udb.services.commons.entities.Music;
import sv.com.udb.services.commons.repository.IGenreRepository;
import sv.com.udb.services.commons.repository.IMusicRepository;
import sv.com.udb.youapp.services.storage.exceptions.InvalidGenereException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/storage/music")
public class MusicController {
   @NonNull
   private final IMusicRepository musicRepository;
   @NonNull
   private final IGenreRepository genreRepository;

   @GetMapping("/")
   public List<Music> music() {
      return musicRepository.findAll();
   }

   @GetMapping("/genre/{genreId}")
   public Set<Music> musicByGenre(@PathVariable Integer genreId) {
      Optional<Genre> genre = genreRepository.getAllById(genreId);
      if (!genre.isPresent()) {
         throw new InvalidGenereException();
      }
      return genre.get().getMusic();
   }
}
