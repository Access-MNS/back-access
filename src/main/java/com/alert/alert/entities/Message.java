package com.alert.alert.entities;

import com.alert.alert.entities.enums.Action;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message extends Auditable{
    public Message(Action action) {
        this.action = action;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne @JoinColumn(name = "sender", referencedColumnName = "id")
    private User sender;
    private String comment;
    private Action action;


    boolean isDeleted = false;

}
