package sv.com.udb.services.authentication.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.repository.PrincipalRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {
    @NonNull
    private final PrincipalRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s)
            throws UsernameNotFoundException {
        Optional<YouAppPrincipal> u = userRepository.findByUsernameOrEmail(s,
                s);
        if (!u.isPresent())
            throw new UsernameNotFoundException(s + " could not be found");
        return u.get();
    }
}
