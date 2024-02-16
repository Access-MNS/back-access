package com.alert.alert.model;

import com.alert.alert.model.enums.Action;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender", referencedColumnName = "id")
    private User senderID;

    private String comment;

    private Action action;

    private Instant timestamp;

    boolean isDeleted = false;

}
