package com.alert.alert.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "channels")
public class Channel extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ChannelsUsers> users = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) @JsonIgnore
    @JoinColumn(name = "parent_channel_id", referencedColumnName = "id")
    private Channel parentChannelId;

    @OneToMany(mappedBy = "parentChannelId") @JsonIgnore
    private Set<Channel> childChannelsId = new HashSet<>();

    public void addChannelUser(User user, boolean canEdit, boolean canDelete, boolean canView, boolean canInvite) {

        ChannelsUsers channelsUsers = new ChannelsUsers(this, user);
        updateProperties(channelsUsers, canEdit, canDelete, canView, canInvite);
        users.add(channelsUsers);
    }

    public void removeChannelUser(User user) {
        for (Iterator<ChannelsUsers> iterator = users.iterator();
             iterator.hasNext(); ) {
            ChannelsUsers channelsUsers = iterator.next();

            if(channelsUsers.user().equals(user)
                    && channelsUsers.channel().equals(this)) {
                iterator.remove();
                channelsUsers.channel(null);
                channelsUsers.user(null);
            }
        }
    }

    public void updateProperties(ChannelsUsers channelsUsers,
                                 boolean canEdit, boolean canDelete, boolean canView, boolean canInvite) {
        channelsUsers
                .canInvite(canInvite)
                .canDelete(canDelete)
                .canEdit(canEdit)
                .canView(canView);
    }
}
