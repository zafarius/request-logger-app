package app.repository.tracker;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
        schema = "app",
        name = "request",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "requestId", "createdId"
                })
        }
)
public class RequestEntity {
    @NotNull
    @Id
    @Column(nullable = false, updatable = false)
    @ToString.Include
    private UUID id;

    @NotNull
    @Column(name = "request_id", nullable = false, updatable = false)
    @ToString.Include
    private Integer requestId;


    @Column(nullable = false, updatable = false)
    private ZonedDateTime created;

    @Column(name = "created_id", nullable = false, updatable = false)
    private String createdId;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}
