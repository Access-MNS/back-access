package com.alert.alert.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "channels")
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;

    @ManyToMany(mappedBy = "channels")
    private Set<User> users;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_channel_id", referencedColumnName = "id")
    private Channel parentChannelId;

    @OneToMany(mappedBy = "parentChannelId", cascade = CascadeType.ALL)
    private Set<Channel> childChannelsId;

    @CreatedDate @CreationTimestamp
    private LocalDateTime createdOn;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate @CreationTimestamp
    private LocalDateTime lastModifiedOn;
    @LastModifiedBy
    private String lastModifiedBy;
}
