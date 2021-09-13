package sv.com.udb.services.authentication.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sv.com.udb.services.authentication.entities.User;
import sv.com.udb.services.authentication.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {

  @NonNull
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    return new User(s);
  }

}
