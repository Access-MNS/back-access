package com.alert.alert.entities;

import com.alert.alert.entities.enums.Role;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter @Setter
@Accessors(chain = true)
@Builder @NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
public class User extends Auditable implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    private long id;

    @JsonView(Views.Public.class)
    private String lastName;

    @JsonView(Views.Public.class)
    private String firstName;

    @Column(unique = true)
    @JsonView(Views.Public.class)
    private String mail;

    private String password;

    @JsonView(Views.Public.class)
    private LocalDateTime lastSeen;

    @JsonView(Views.Public.class)
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
