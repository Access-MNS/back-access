package com.alert.alert.entities;

import com.alert.alert.entities.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
public class User extends Auditable implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String lastName;

    private String firstName;

    @Column(unique = true)
    private String mail;

    private String password;

    private LocalDateTime lastSeen;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "sentTo")
    private Set<Message> gotMessage = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return mail;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
