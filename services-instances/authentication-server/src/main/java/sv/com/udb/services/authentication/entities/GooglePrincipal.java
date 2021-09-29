package sv.com.udb.services.authentication.entities;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class GooglePrincipal implements Serializable {
    private String          idToken;
    private List<String>    scopes;
    private String          serverAuthCode;
    private YouAppPrincipal user;
}
