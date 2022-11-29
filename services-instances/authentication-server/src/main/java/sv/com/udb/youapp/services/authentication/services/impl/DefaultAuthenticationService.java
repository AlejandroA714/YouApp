package sv.com.udb.youapp.services.authentication.services.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sv.com.udb.youapp.commons.jpa.services.PrincipalService;
import sv.com.udb.youapp.services.authentication.services.AuthenticationService;

@Slf4j
@RequiredArgsConstructor
public class DefaultAuthenticationService implements AuthenticationService {

  @NonNull
  private final PrincipalService principalService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return principalService.findByUsername(username);
  }
}
