package org.notification_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "push_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushToken {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String deviceToken;

    @Column(nullable = false)
    private UUID userId;

    private String deviceType; // Android, iOS, Web
    private boolean active = true;
}

