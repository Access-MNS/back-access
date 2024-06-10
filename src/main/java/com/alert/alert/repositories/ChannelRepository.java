package com.alert.alert.repositories;

import com.alert.alert.entities.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    @Query("select c from Channel c where c.isDeleted = false and c.id = :id")
    Optional<Channel> findByIdWhereIsNotDeleted(Long id);

    @Query("select c from Channel c where c.isDeleted = false")
    Collection<Channel> findAllWhereIsNotDeleted();
}
