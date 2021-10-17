package sv.com.udb.components.web.client.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebClientParameter {
   private String name;
   private Object value;
}