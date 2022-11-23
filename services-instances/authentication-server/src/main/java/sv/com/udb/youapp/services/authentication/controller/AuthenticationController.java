package sv.com.udb.youapp.services.authentication.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.com.udb.youapp.services.authentication.dto.User;
import sv.com.udb.youapp.services.authentication.entities.UserEntity;
import sv.com.udb.youapp.services.authentication.repositories.RoleRepository;
import sv.com.udb.youapp.services.authentication.repositories.UserRepository;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthenticationController {
   @NonNull
   private final UserRepository userRepository;
   @NonNull
   private final RoleRepository roleRepository;
   @NonNull
   private final ModelMapper    modelMapper;

   @GetMapping("/")
   private Object test() {
      var sss = userRepository.findAll();
      var dssa = roleRepository.findAllWithRoles();
      var ddd = sss.stream().map(x -> modelMapper.map(x,User.class)).toList();
      //var osos = modelMapper.map(sas, User.class);
      return ddd;
   }

   @PostMapping("/")
   private User iser(@RequestBody @Valid User user){
      var ddd  = modelMapper.map(user, UserEntity.class);
      userRepository.save(ddd);
      return user;
   }

}
