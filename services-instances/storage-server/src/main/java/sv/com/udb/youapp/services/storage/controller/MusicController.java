package sv.com.udb.youapp.services.storage.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sv.com.udb.services.commons.entities.Genre;
import sv.com.udb.services.commons.entities.Music;
import sv.com.udb.services.commons.entities.Playlist;
import sv.com.udb.services.commons.entities.YouAppPrincipal;
import sv.com.udb.services.commons.exceptions.PrincipalDoesNotExist;
import sv.com.udb.services.commons.repository.IGenreRepository;
import sv.com.udb.services.commons.repository.IMusicRepository;
import sv.com.udb.services.commons.repository.IPlaylistRepository;
import sv.com.udb.services.commons.repository.IPrincipalRepository;
import sv.com.udb.youapp.services.storage.exceptions.InvalidGenereException;
import sv.com.udb.youapp.services.storage.exceptions.InvalidMusicException;
import sv.com.udb.youapp.services.storage.exceptions.InvalidPlaylistException;
import sv.com.udb.youapp.services.storage.models.CreatePlaylistRequest;
import sv.com.udb.youapp.services.storage.models.PlaylistRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/storage/music")
public class MusicController {
   @NonNull
   private final IMusicRepository     musicRepository;
   @NonNull
   private final IGenreRepository     genreRepository;
   @NonNull
   private final IPrincipalRepository principalRepository;
   @NonNull
   private final IPlaylistRepository  playlistRepository;
   private static final String        UUID_CLAIM = "id";

   @GetMapping("/playlist")
   public List<Playlist> playlist(JwtAuthenticationToken principal) {
      String uuid = principal.getToken().getClaimAsString(UUID_CLAIM);
      return playlistRepository.findByUserIdWithSongs(uuid);
   }

   @PostMapping("/playlist/add")
   public Playlist addToPlaylist(
         @NotNull @RequestBody CreatePlaylistRequest request,
         JwtAuthenticationToken principal) {
      String uuid = principal.getToken().getClaimAsString(UUID_CLAIM);
      YouAppPrincipal p = principalRepository.getById(uuid);
      Playlist playlist = Playlist.builder().title(request.getTitle()).user(p)
            .build();
      playlistRepository.saveAndFlush(playlist);
      return playlist;
   }

   @PostMapping("/playlist")
   public void addToPlaylist(@Valid @RequestBody PlaylistRequest request,
         JwtAuthenticationToken principal) {
      Optional<Playlist> playlist = playlistRepository
            .findByIdWithSongs(request.getPlaylistId());
      if (!playlist.isPresent()) {
         throw new InvalidPlaylistException();
      }
      Playlist play = playlist.get();
      Optional<Music> music = musicRepository.findById(request.getMusicId());
      if (!music.isPresent()) {
         throw new InvalidMusicException();
      }
      play.getSongs().add(music.get());
      playlistRepository.saveAndFlush(play);
   }

   @PostMapping("/playlist/remove")
   public void remoteFromPlaylist(@Valid @RequestBody PlaylistRequest request,
         JwtAuthenticationToken principal) {
      Optional<Playlist> playlist = playlistRepository
            .findByIdWithSongs(request.getPlaylistId());
      if (!playlist.isPresent()) {
         throw new InvalidPlaylistException();
      }
      Playlist play = playlist.get();
      Optional<Music> music = musicRepository.findById(request.getMusicId());
      if (!music.isPresent()) {
         throw new InvalidMusicException();
      }
      play.getSongs().remove(music.get());
      playlistRepository.saveAndFlush(play);
   }

   @GetMapping("/favorites")
   public Object favorites(JwtAuthenticationToken principal) {
      String uuid = principal.getToken().getClaimAsString(UUID_CLAIM);
      Optional<YouAppPrincipal> p = principalRepository
            .findByIdWithFavorities(uuid);
      if (!p.isPresent()) {
         throw new PrincipalDoesNotExist(uuid + " does not exits");
      }
      return p.get().getFavorities().stream().map(x -> {
         x.setLikes(true);
         return x;
      }).collect(Collectors.toList());
   }

   @GetMapping("/like/{id}")
   public void like(@PathVariable("id") int id,
         JwtAuthenticationToken principal) {
      String uuid = principal.getToken().getClaimAsString(UUID_CLAIM);
      Optional<Music> music = musicRepository.findById(id);
      if (!music.isPresent()) {
         throw new InvalidMusicException();
      }
      Music m = music.get();
      Optional<YouAppPrincipal> p = principalRepository
            .findByIdWithFavorities(uuid);
      if (!p.isPresent()) {
         throw new PrincipalDoesNotExist(uuid + " does not exits");
      }
      YouAppPrincipal pr = p.get();
      pr.getFavorities().add(m);
      principalRepository.saveAndFlush(pr);
   }

   @GetMapping("/dislike/{id}")
   public void dislike(@PathVariable("id") int id,
         JwtAuthenticationToken principal) {
      String uuid = principal.getToken().getClaimAsString(UUID_CLAIM);
      Optional<Music> music = musicRepository.findById(id);
      if (!music.isPresent()) {
         throw new InvalidMusicException();
      }
      Music m = music.get();
      Optional<YouAppPrincipal> p = principalRepository
            .findByIdWithFavorities(uuid);
      if (!p.isPresent()) {
         throw new PrincipalDoesNotExist(uuid + " does not exits");
      }
      YouAppPrincipal pr = p.get();
      pr.getFavorities().remove(m);
      principalRepository.saveAndFlush(pr);
   }

   @GetMapping("/")
   public Collection<Music> music(JwtAuthenticationToken principal) {
      try {
         String uuid = principal.getToken().getClaimAsString(UUID_CLAIM);
         return flagFavorities(musicRepository.findAll(), uuid);
      }
      catch (Exception e) {
         throw e;
      }
   }

   @GetMapping("/find")
   public Collection<Music> findMusic(@RequestParam String title,
         JwtAuthenticationToken principal) {
      String uuid = principal.getToken().getClaimAsString(UUID_CLAIM);
      return flagFavorities(musicRepository.findAllByTitleContains(title),
            uuid);
   }

   @GetMapping("/genre/{genreId}")
   public Collection<Music> musicByGenre(@PathVariable Integer genreId,
         JwtAuthenticationToken principal) {
      String uuid = principal.getToken().getClaimAsString(UUID_CLAIM);
      Optional<Genre> genre = genreRepository.getAllById(genreId);
      if (!genre.isPresent()) {
         throw new InvalidGenereException();
      }
      return flagFavorities(genre.get().getMusic(), uuid);
   }

   private Collection<Music> flagFavorities(Collection<Music> music,
         String uuid) {
      return music.stream().map(m -> {
         m.setLikes(m.getUserFavorites().stream().map(x -> x.getId())
               .collect(Collectors.toList()).contains(uuid));
         return m;
      }).collect(Collectors.toList());
   }
}
