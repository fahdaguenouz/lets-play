package com.lets_play.api.products.repo;

import com.lets_play.api.products.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByUserId(String userId);
    void deleteAllByUserId(String userId);

}
