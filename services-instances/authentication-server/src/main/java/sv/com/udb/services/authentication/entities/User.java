package sv.com.udb.services.authentication.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;

@Data
@Builder
@Entity(name = "User")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(name = "active")
    private boolean accountNonExpired;
    @Column(name = "user_name", unique = true)
    private String  username;
    @Column
    private String  password;
    @Column
    private String  roles;

    public User(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("APP_USER"));
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.accountNonExpired;
    }
}
