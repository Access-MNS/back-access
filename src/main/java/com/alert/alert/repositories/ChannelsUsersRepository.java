package com.alert.alert.repositories;

import com.alert.alert.entities.ChannelsUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelsUsersRepository extends JpaRepository<ChannelsUsers, Long> {
    public Optional<ChannelsUsers> findByUserIdAndChannelId(Long userId, Long channelId);
    public boolean existsByUserIdAndChannelId(Long userId, Long channelId);
}
