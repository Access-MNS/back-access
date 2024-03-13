package com.alert.alert.entities;

import com.alert.alert.entities.enums.Action;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@Accessors(chain = true)
@NoArgsConstructor @AllArgsConstructor
@Table(name = "messages")
public class Message extends Auditable{

    public Message(Action action) {
        this.action = action;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "sender", referencedColumnName = "id")
    private User sender;

    private String comment;

    private Action action;

    @ManyToOne
    Channel channel;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "messages_not_seen",
        joinColumns = @JoinColumn(name = "message_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> sentTo = new HashSet<>();

    boolean isDeleted = false;

}
