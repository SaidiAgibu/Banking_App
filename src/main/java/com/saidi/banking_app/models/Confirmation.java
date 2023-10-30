package com.saidi.banking_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Confirmation {
    @Id
    @SequenceGenerator(
            name = "confirmation_sequence",
            sequenceName = "confirmation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "confirmation_sequence")
    private Long id;
    private String token;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private LocalDateTime createdAt;

    public Confirmation(User user) {
        this.token = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.user = user;
    }

}
