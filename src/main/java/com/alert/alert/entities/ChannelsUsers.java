package com.alert.alert.entities;

import com.alert.alert.entities.keys.ChannelsUsersKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(fluent = true, chain = true)
@NoArgsConstructor
public class ChannelsUsers extends Auditable{

    @EmbeddedId
    private ChannelsUsersKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("channelId")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    private boolean canEdit = false;
    private boolean canDelete = false;
    private boolean canView = true;
    private boolean canInvite = false;

    public ChannelsUsers(Channel channel, User user) {
        this.channel = channel;
        this.user = user;
        this.id = new ChannelsUsersKey(channel.getId(), user.getId());
    }
}
