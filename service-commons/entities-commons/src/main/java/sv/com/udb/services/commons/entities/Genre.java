package sv.com.udb.services.commons.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "genre")
@ToString(exclude = "music")
@EqualsAndHashCode(exclude = "music")
@NamedEntityGraph(name = "music_by_genre",
                  attributeNodes = @NamedAttributeNode(value = "music",
                                                       subgraph = "music_by_genre_favorites"),
                  subgraphs = @NamedSubgraph(name = "music_by_genre_favorites",
                                             attributeNodes = @NamedAttributeNode("userFavorites")))
public class Genre {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer    id;
   @Column(length = 32, nullable = false)
   private String     title;
   @JsonBackReference
   @OneToMany(mappedBy = "genre")
   private Set<Music> music;
}
