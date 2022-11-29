package sv.com.udb.youapp.services.authentication.services.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sv.com.udb.youapp.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.youapp.services.authentication.services.EncryptPasswordService;
import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class DefaultEncryptPasswordService implements EncryptPasswordService {

  private static final String            RSA                   = "RSA";
  private static final String            RSA_ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";
  private static final char[]            characters            = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?"
      .toCharArray();
  @NonNull
  private final AuthenticationProperties authProperties;

  @Override
  public String encode(CharSequence charSequence) {
    try {
      return encryptPassword(charSequence.toString());
    }
    catch (Exception e) {
      return "";
    }
  }

  @Override
  public boolean matches(CharSequence charSequence, String s) {
    try {
      return decryptPassword(s).equals(charSequence.toString());
    }
    catch (Exception e) {
      LOGGER.error("Failed to decrypt due", e);
      return false;
    }
  }

  @Override
  public String encryptPassword(String payload) throws NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
      IllegalBlockSizeException, BadPaddingException {
    Cipher cipherEncrypt = Cipher.getInstance(RSA_ECB_PKCS1_PADDING);
    cipherEncrypt.init(Cipher.ENCRYPT_MODE,
        getPublicKey(authProperties.getKeys().getPublicKey()));
    try {
      return Base64.getEncoder()
          .encodeToString(cipherEncrypt.doFinal(payload.getBytes()));
    }
    finally {
      cipherEncrypt = null;
    }
  }

  @Override
  public String decryptPassword(String payload) throws NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
      IllegalBlockSizeException, BadPaddingException {
    var cipherDecrypt = Cipher.getInstance(RSA_ECB_PKCS1_PADDING);
    cipherDecrypt.init(Cipher.DECRYPT_MODE,
        getPrivateKey(authProperties.getKeys().getPrivateKey()));
    try {
      return new String(
          cipherDecrypt.doFinal(Base64.getDecoder().decode(payload)));
    }
    finally {
      cipherDecrypt = null;
    }
  }

  @Override
  public String generateRandomPassword(int length) {
    return RandomStringUtils.random(length, 0, characters.length - 1, true,
        true, characters, new SecureRandom());
  }

  private Key getPublicKey(String publicKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
        Base64.getDecoder().decode(publicKey));
    KeyFactory keyFactory = KeyFactory.getInstance(RSA);
    return keyFactory.generatePublic(keySpec);
  }

  private Key getPrivateKey(String privateKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
        Base64.getDecoder().decode(privateKey));
    KeyFactory keyFactory = KeyFactory.getInstance(RSA);
    return keyFactory.generatePrivate(keySpec);
  }
}
