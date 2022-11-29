package sv.com.udb.youapp.commons.jpa.exceptions;

public class UserNotFoundException extends RuntimeException{

  public UserNotFoundException(){
    super("User has not been found");
  }

}
