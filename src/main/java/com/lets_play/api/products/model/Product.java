package com.lets_play.api.products.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "products")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Product {

    @Id
    private String id;

    private String name;
    private String description;

    private Double price;

    // owner
    @Indexed
    private String userId;

    private Instant createdAt;
    private Instant updatedAt;
}
