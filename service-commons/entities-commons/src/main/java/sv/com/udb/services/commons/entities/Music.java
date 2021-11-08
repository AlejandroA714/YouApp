package sv.com.udb.services.commons.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "music")
@ToString(exclude = { "user", "playlist", "userFavorites" })
@EqualsAndHashCode(exclude = { "user", "playlist", "userFavorites" })
@NamedEntityGraphs(value = {
      @NamedEntityGraph(name = "music_playlist",
                        attributeNodes = @NamedAttributeNode(value = "playlist")),
      @NamedEntityGraph(name = "music_favorites",
                        attributeNodes = @NamedAttributeNode(value = "userFavorites")) })
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
   @Column(length = 512)
   protected String             photo;
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
   @JsonBackReference
   @ManyToMany(mappedBy = "favorities")
   private Set<YouAppPrincipal> userFavorites;
   @Transient
   @JsonSerialize
   private boolean              likes = false;
}
