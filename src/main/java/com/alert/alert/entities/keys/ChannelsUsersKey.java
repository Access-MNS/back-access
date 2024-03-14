package com.alert.alert.entities.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ChannelsUsersKey implements Serializable {

    @Column(name = "channel_id")
    private Long channelId;

    @Column(name = "user_id")
    private Long userId;

    public ChannelsUsersKey(Long channelId, Long userId) {
        this.channelId = channelId;
        this.userId = userId;
    }

    public ChannelsUsersKey() {}
}
