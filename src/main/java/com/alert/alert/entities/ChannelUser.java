package com.alert.alert.entities;

import com.alert.alert.entities.keys.ChannelsUsersKey;
import com.fasterxml.jackson.annotation.JsonView;
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
public class ChannelUser extends Auditable{

    @EmbeddedId
    private ChannelsUsersKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("channelId")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @JsonView(Views.Public.class)
    private boolean canEdit = false;
    @JsonView(Views.Public.class)
    private boolean canDelete = false;
    @JsonView(Views.Public.class)
    private boolean canView = true;
    @JsonView(Views.Public.class)
    private boolean canInvite = false;

    public ChannelUser(Channel channel, User user) {
        this.channel = channel;
        this.user = user;
        this.id = new ChannelsUsersKey(channel.getId(), user.getId());
    }
}
