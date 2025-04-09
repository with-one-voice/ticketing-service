package com.onevoice.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    protected UUID createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    protected UUID updatedBy;

    @Column(name ="deleted_at")
    protected LocalDateTime deletedAt;

    @Column(name ="deleted_by")
    protected UUID deletedBy;

    /**
     * soft delete
     */
    public void delete(UUID username){
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = username;
    }

    public void restore(){
        this.deletedAt = null;
        this.deletedBy = null;
    }

    public boolean isDeleted(){
        return deletedAt != null;
    }
}
