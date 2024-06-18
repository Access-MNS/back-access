package com.alert.alert.service.message;

import com.alert.alert.entity.Message;

import java.util.Collection;

public interface MessageNotSeenService {

    Collection<Message> getMessagesNotSeen(Long id);
    boolean deleteMessageNotSeen(Long userId, Long channelId);
}
