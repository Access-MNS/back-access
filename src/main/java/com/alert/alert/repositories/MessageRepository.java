package com.alert.alert.repositories;

import com.alert.alert.entities.Message;
import com.alert.alert.entities.enums.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message getMessageByAction(Action action);
}
