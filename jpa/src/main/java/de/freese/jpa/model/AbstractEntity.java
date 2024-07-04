// Created: 11.03.24
package de.freese.jpa.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
// import org.springframework.data.annotation.CreatedDate;
// import org.springframework.data.annotation.LastModifiedDate;

/**
 * @author Thomas Freese
 */
@MappedSuperclass
public abstract class AbstractEntity {

    // @Id
    // @GeneratedValue
    // private UUID id;

    // @Temporal(TemporalType.TIME)
    // private java.util.Date utilTime;
    //
    // @Temporal(TemporalType.DATE)
    // private java.util.Date utilDate;
    //
    // @Temporal(TemporalType.TIMESTAMP)
    // private java.util.Date utilTimestamp;

    // @JsonIgnore
    // @NotNull(message = "CREATED Timestamp can not be blank")
    // @CreatedDate
    @CreationTimestamp
    @Column(name = "CREATED", nullable = false, columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    private LocalDateTime created;

    // @JsonIgnore
    // @NotNull(message = "UPDATED Timestamp can not be blank")
    // @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "UPDATED", columnDefinition = "TIMESTAMP on update CURRENT_TIMESTAMP")
    private LocalDateTime updated;

    // @JsonIgnore
    @Version
    private Integer version;

    public LocalDateTime getCreated() {
        return created;
    }

    // public UUID getId() {
    //     return id;
    // }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public Integer getVersion() {
        return version;
    }
}
