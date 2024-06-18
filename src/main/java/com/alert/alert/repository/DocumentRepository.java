package com.alert.alert.repository;

import com.alert.alert.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    boolean existsByName(String name);

    Optional<Document> findByUserId(long userId);
}
