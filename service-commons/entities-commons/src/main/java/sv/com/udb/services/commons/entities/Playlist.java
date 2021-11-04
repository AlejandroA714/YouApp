package sv.com.udb.services.commons.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
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
   @Column(nullable = false)
   private Integer         id;
   @Column(nullable = false, length = 64)
   private String          title;
   @ManyToOne
   @JsonBackReference
   private YouAppPrincipal user;
   @ManyToMany
   @JsonManagedReference
   @JoinTable(name = "playlist_song",
              joinColumns = @JoinColumn(name = "playlist_id",
                                        referencedColumnName = "id"),
              inverseJoinColumns = @JoinColumn(name = "music_id",
                                               referencedColumnName = "id"))
   private Set<Music>      songs;
}
