package sv.com.udb.services.authentication.entities;

import sv.com.udb.services.authentication.enums.IOAuthRegistrationType;
import sv.com.udb.services.authentication.enums.IRole;

import java.time.LocalDate;
import java.time.ZoneId;

public interface Principal {
   String getId();

   String getEmail();

   String getNombres();

   String getApellidos();

   String getUsername();

   String getPhoto();

   LocalDate getBirthday();

   boolean isActive();

   default LocalDate getRegistration() {
      return LocalDate.now(ZoneId.of("GMT-06:00"));
   }

   default IOAuthRegistrationType getOAuthRegistrationType() {
      return IOAuthRegistrationType.YOUAPP;
   }

   default IRole getRol() {
      return IRole.ROLE_USER;
   }
}
