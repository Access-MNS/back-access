package com.alert.alert.repository;

import com.alert.alert.entity.Message;
import com.alert.alert.entity.enums.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message getMessageByAction(Action action);
    Message findByChannelId(Long channelId);
    List<Message> getMessagesByChannel_Id(Long id);
    List<Message> getMessagesByIsDeletedIsTrue();

    @Query("select distinct m from Message m join m.sentTo mst where mst.id = :id")
    List<Message> getMessagesNotSeenByUserId(Long id);
}
