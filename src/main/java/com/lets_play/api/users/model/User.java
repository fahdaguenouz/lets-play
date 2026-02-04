package com.lets_play.api.users.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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

    @Field("email")
    @Indexed(unique = true)
    private String email;

    @Field("username")
    @Indexed(unique = true)
    private String username;

    @Field("name")
    private String name;

    @Field("password_hash")
    private String passwordHash;

    @Field("role")
    private Role role;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;
}
