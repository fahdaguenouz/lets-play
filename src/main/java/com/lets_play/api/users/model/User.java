package com.lets_play.api.users.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String username;

    private String name;

    // NEVER return this in responses (DTO only)
    private String passwordHash;

    private Role role;

    // optional: to support bans later
    private String status; // e.g. "ACTIVE", "BANNED"

    private Instant createdAt;
    private Instant updatedAt;
}
