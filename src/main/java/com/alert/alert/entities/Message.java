package com.alert.alert.entities;

import com.alert.alert.entities.enums.Action;
import com.fasterxml.jackson.annotation.JsonView;
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
    @JsonView(Views.Public.class)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender", referencedColumnName = "id")
    @JsonView(Views.Public.class)
    private User sender;

    @JsonView(Views.Public.class)
    private String comment;

    @JsonView(Views.Public.class)
    private Action action;

    @ManyToOne
    @JsonView(Views.Public.class)
    Channel channel;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "messages_not_seen",
        joinColumns = @JoinColumn(name = "message_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> sentTo = new HashSet<>();

    @JsonView(Views.Public.class)
    boolean isDeleted = false;
}
