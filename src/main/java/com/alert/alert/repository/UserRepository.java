package com.alert.alert.repository;

import com.alert.alert.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByMail(String email);
    Optional<User> findByMail(String email);
}

