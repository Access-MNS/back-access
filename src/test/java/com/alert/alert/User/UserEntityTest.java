package com.alert.alert.User;

import com.alert.alert.entity.User;
import com.alert.alert.entity.enums.Role;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class UserEntityTest {

    @Test
    public void testUserAuthorities() {
        User user = new User()
                .setId(1L)
                .setLastName("Doe")
                .setFirstName("John")
                .setMail("john.doe@example.com")
                .setPassword("password")
                .setRole(Role.USER);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void testUserDetails() {
        User user = new User()
                .setId(1L)
                .setLastName("Doe")
                .setFirstName("John")
                .setMail("john.doe@example.com")
                .setPassword("password")
                .setRole(Role.USER);

        assertEquals("john.doe@example.com", user.getUsername());
        assertEquals("password", user.getPassword());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }
}
