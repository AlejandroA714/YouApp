package sv.com.udb.services.commons.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sv.com.udb.services.commons.enums.IStatus;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "status")
@ToString(exclude = "music")
@EqualsAndHashCode(exclude = "music")
@NamedEntityGraph(name = "music_by_status",
                  attributeNodes = @NamedAttributeNode(value = "music"))
public class Status {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer    id;
   @Column(length = 32, nullable = false)
   @Enumerated(EnumType.STRING)
   private IStatus    status;
   @JsonBackReference
   @OneToMany(mappedBy = "status")
   private Set<Music> music;
}
