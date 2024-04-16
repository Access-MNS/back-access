package com.alert.alert.repositories;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.ChannelsUsers;
import com.alert.alert.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ChannelsUsersRepository extends JpaRepository<ChannelsUsers, Long> {

    Optional<ChannelsUsers> findByUserIdAndChannelId(Long userId, Long channelId);

    boolean existsByUserIdAndChannelId(Long userId, Long channelId);

    @Query("select u from User u left join ChannelsUsers c on u.id = c.id.userId where c.id.channelId = :channelId")
    Set<User> getChannelsUsersByChannelId(Long channelId);

    @Query("select c from Channel c left join ChannelsUsers cu on c.id = cu.id.channelId where cu.id.userId = :userId")
    Set<Channel> getChannelsByUserId(Long userId);

}
