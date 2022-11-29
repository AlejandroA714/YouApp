package sv.com.udb.youapp.commons.jpa.services.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sv.com.udb.youapp.commons.jpa.dto.User;
import sv.com.udb.youapp.commons.jpa.entities.PrincipalEntity;
import sv.com.udb.youapp.commons.jpa.exceptions.UserNotFoundException;
import sv.com.udb.youapp.commons.jpa.repositories.PrincipalRepository;
import sv.com.udb.youapp.commons.jpa.services.PrincipalService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class DefaultPrincipalService implements PrincipalService {

  @NonNull
  private final PrincipalRepository principalRepository;

  @Override
  public List<User> findAll() {
    return principalRepository.findAll().stream().map(User::map).toList();
  }

  @Override
  public User findByUsername(String username) {
    return principalRepository.findByUsername(username)
        .flatMap((PrincipalEntity entity) -> Optional.of(User.map(entity)))
        .orElseThrow(UserNotFoundException::new);

  }
}
