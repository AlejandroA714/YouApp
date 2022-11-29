package sv.com.udb.youapp.commons.jpa.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sv.com.udb.youapp.commons.jpa.dto.User;
import sv.com.udb.youapp.commons.jpa.repositories.PrincipalRepository;

import java.util.List;

public interface PrincipalService {

  List<User> findAll();

  User findByUsername(String username);


}
