package sv.com.udb.services.commons.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "playlist")
@ToString(exclude = { "user", "songs" })
@EqualsAndHashCode(exclude = { "user", "songs" })
@NamedEntityGraph(name = "playlist_song",
                  attributeNodes = @NamedAttributeNode(value = "songs"))
public class Playlist {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(nullable = false)
   private Integer         id;
   @Column(nullable = false, length = 64)
   private String          title;
   @ManyToOne
   @JsonManagedReference
   private YouAppPrincipal user;
   @ManyToMany
   @JoinTable(name = "playlist_song",
              joinColumns = @JoinColumn(name = "playlist_id",
                                        referencedColumnName = "id"),
              inverseJoinColumns = @JoinColumn(name = "music_id",
                                               referencedColumnName = "id"))
   private Set<Music>      songs;
}
