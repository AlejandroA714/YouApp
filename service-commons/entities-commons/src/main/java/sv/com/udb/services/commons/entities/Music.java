package sv.com.udb.services.commons.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "music")
@ToString(exclude = { "user", "playlist", "user_favorites" })
@EqualsAndHashCode(exclude = { "user", "playlist", "user_favorites" })
@NamedEntityGraphs(value = {
      @NamedEntityGraph(name = "music_playlist",
                        attributeNodes = @NamedAttributeNode(value = "playlist")),
      @NamedEntityGraph(name = "music_favorites",
                        attributeNodes = @NamedAttributeNode(value = "user_favorites")) })
public class Music {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer              id;
   @Column(length = 128)
   private String               title;
   @Column
   private int                  duration;
   @Column(name = "song_url", length = 256)
   private String               uri;
   @ManyToOne
   @JsonManagedReference
   private Status               status;
   @ManyToOne
   @JsonManagedReference
   private Genre                genre;
   @ManyToOne
   @JsonManagedReference
   private YouAppPrincipal      user;
   @ManyToMany(mappedBy = "songs")
   private Set<Playlist>        playlist;
   @ManyToMany(mappedBy = "favorities")
   private Set<YouAppPrincipal> user_favorites;
}
