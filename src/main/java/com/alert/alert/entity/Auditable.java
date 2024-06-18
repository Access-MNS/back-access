package com.alert.alert.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true)
@MappedSuperclass
@Getter
@Setter
public abstract class Auditable {

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    @Temporal(TIMESTAMP)
    @JsonView(Views.Public.class)
    private Date createdDate;

    @Column(name="modified_date")
    @LastModifiedDate
    @Temporal(TIMESTAMP)
    @JsonView(Views.Public.class)
    private Date modifiedDate;

    @Column(name = "created_by", nullable = false, updatable = false)
    @CreatedBy
    @JsonView(Views.Public.class)
    private String createdBy;

    @Column(name = "modified_by")
    @LastModifiedBy
    @JsonView(Views.Public.class)
    private String modifiedBy;

    @Column(name = "is_deleted", nullable = false)
    @JsonView(Views.Public.class)
    private boolean isDeleted = false;
}

