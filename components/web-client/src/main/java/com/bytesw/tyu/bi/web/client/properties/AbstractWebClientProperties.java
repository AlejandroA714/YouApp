package com.bytesw.tyu.bi.web.client.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public abstract class AbstractWebClientProperties {
   @NotNull
   @URL(message = "Invalid URL")
   private java.net.URL remoteAddress;

   public String getRemoteAddress() {
      return remoteAddress.toString();
   }

   public int getPort() {
      if (null != remoteAddress) return remoteAddress.getPort();
      else
         return remoteAddress.getDefaultPort();
   }

   public String getHost() {
      if (null != remoteAddress) return remoteAddress.getHost();
      else
         return "";
   }
}
