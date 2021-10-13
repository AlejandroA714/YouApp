package sv.com.udb.services.authentication.entities;

import sv.com.udb.services.authentication.enums.IOAuthRegistrationType;
import sv.com.udb.services.authentication.enums.IRole;

import java.time.LocalDate;

public interface Principal {
   String getId();

   String getEmail();

   String getNombres();

   String getApellidos();

   String getUsername();

   String getPhoto();

   LocalDate getBirthday();

   boolean isActive();

   default IOAuthRegistrationType getOAuthRegistrationType() {
      return IOAuthRegistrationType.YOUAPP;
   }

   default IRole getRol() {
      return IRole.ROLE_USER;
   }
}
