package com.lets_play.api.users.repo;



import org.springframework.data.mongodb.repository.MongoRepository;

import com.lets_play.api.users.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
