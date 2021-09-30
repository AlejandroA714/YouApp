package sv.com.udb.services.authentication.services;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface IEncryptionPasswordService extends PasswordEncoder {
   String encryptPassword(String payload) throws NoSuchPaddingException,
         NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
         IllegalBlockSizeException, BadPaddingException;

   String decryptPassword(String payload) throws NoSuchPaddingException,
         NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
         IllegalBlockSizeException, BadPaddingException;
}
