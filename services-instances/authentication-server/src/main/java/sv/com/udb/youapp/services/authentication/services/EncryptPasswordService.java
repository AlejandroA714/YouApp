package sv.com.udb.youapp.services.authentication.services;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface EncryptPasswordService extends PasswordEncoder {

  String encryptPassword(String payload) throws NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
      IllegalBlockSizeException, BadPaddingException;

  String decryptPassword(String payload) throws NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
      IllegalBlockSizeException, BadPaddingException;

  String generateRandomPassword(int length);
}
