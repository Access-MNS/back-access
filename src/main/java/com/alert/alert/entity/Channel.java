package com.alert.alert.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.*;

@Entity
@Getter @Setter
@Accessors(chain = true)
@NoArgsConstructor @AllArgsConstructor
@Table(name = "channels")
public class Channel extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    private long id;

    @JsonView(Views.Public.class)
    private String name;

    @JsonView(Views.Public.class)
    private String description;

    @JsonView(Views.Public.class)
    private Boolean isPrivate = false;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    private List<ChannelUser> users = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_channel_id", referencedColumnName = "id")
    private Channel parentChannelId;

    @OneToMany(mappedBy = "parentChannelId")
    private Set<Channel> childChannelsId = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addChannelUser(User user, boolean canEdit, boolean canDelete, boolean canView, boolean canInvite) {

        ChannelUser channelsUsers = new ChannelUser(this, user);
        updateProperties(channelsUsers, canEdit, canDelete, canView, canInvite);
        users.add(channelsUsers);
    }

    public void removeChannelUser(User user) {
        for (Iterator<ChannelUser> iterator = users.iterator();
             iterator.hasNext(); ) {
            ChannelUser channelsUsers = iterator.next();

            if(channelsUsers.user().equals(user) && channelsUsers.channel().equals(this)) {
                iterator.remove();
                channelsUsers.channel(null);
                channelsUsers.user(null);
            }
        }
    }

    public void updateProperties(ChannelUser channelsUsers,
                                 boolean canEdit, boolean canDelete, boolean canView, boolean canInvite) {
        channelsUsers
                .canInvite(canInvite)
                .canDelete(canDelete)
                .canEdit(canEdit)
                .canView(canView);
    }
}
