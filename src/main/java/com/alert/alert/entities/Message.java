package com.alert.alert.entities;

import com.alert.alert.entities.enums.Action;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {
    public Message(Action action, LocalDateTime createdOn) {
        this.action = action;
        this.createdOn = createdOn;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "sender", referencedColumnName = "id")
    private User sender;
    private String comment;
    private Action action;
    @CreatedDate @CreationTimestamp
    private LocalDateTime createdOn;
    @LastModifiedDate @CreationTimestamp
    private LocalDateTime lastModifiedOn;
    @LastModifiedBy
    private String lastModifiedBy;

    boolean isDeleted = false;

}
