package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sv.com.udb.services.authentication.entities.Privilege;
import sv.com.udb.services.authentication.entities.Role;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.repository.PrincipalRepository;
import sv.com.udb.services.authentication.repository.PrivilegeRepository;
import sv.com.udb.services.authentication.repository.RoleRepository;
import sv.com.udb.services.authentication.services.EncryptionPasswordService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    @NonNull
    private final PrincipalRepository       userRepository;
    @NonNull
    private final RoleRepository            roleRepository;
    @NonNull
    private final PrivilegeRepository       privilegeRepository;
    @NonNull
    private final EncryptionPasswordService encryptionPasswordService;

    @GetMapping("/list")
    public List<YouAppPrincipal> test(@RequestParam String code) {
        LOGGER.info("code {}", code);
        // LOGGER.info("PRINCIPAL: {}",principal);
        var users = userRepository.findAll();
        return users;
    }

    @GetMapping("/role")
    public List<Role> test1() {
        return roleRepository.findAll();
    }

    @GetMapping("/privi")
    public List<Privilege> test2() {
        return privilegeRepository.findAll();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void Conflic(Exception e) {
        LOGGER.error("Failed");
    }

    @GetMapping("/test")
    public String asd() throws NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException,
            InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        return encryptionPasswordService.encryptPassword("9d[?hr%[Y>w~nV3_");
    }
}
