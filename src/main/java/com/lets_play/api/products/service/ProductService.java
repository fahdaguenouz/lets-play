package com.lets_play.api.products.service;

import com.lets_play.api.common.exception.ForbiddenException;
import com.lets_play.api.common.exception.NotFoundException;
import com.lets_play.api.common.exception.UnauthorizedException;
import com.lets_play.api.products.dto.ProductCreateRequest;
import com.lets_play.api.products.dto.ProductResponse;
import com.lets_play.api.products.dto.ProductUpdateRequest;
import com.lets_play.api.products.model.Product;
import com.lets_play.api.products.repo.ProductRepository;
import com.lets_play.api.security.AuthPrincipal;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> listAll() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ProductResponse getOne(String id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return toResponse(p);
    }

    public ProductResponse create(ProductCreateRequest req) {
        String userId = currentUserId();

        Instant now = Instant.now();
        Product p = Product.builder()
                .name(req.name().trim())
                .description(req.description() == null ? null : req.description().trim())
                .price(req.price())
                .userId(userId)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return toResponse(productRepository.save(p));
    }

    public ProductResponse update(String id, ProductUpdateRequest req) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        assertOwnerOrAdmin(p);

        if (req.name() != null)
            p.setName(req.name().trim());
        if (req.description() != null)
            p.setDescription(req.description().trim());
        if (req.price() != null)
            p.setPrice(req.price());

        p.setUpdatedAt(Instant.now());
        return toResponse(productRepository.save(p));
    }

    public void delete(String id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        assertOwnerOrAdmin(p);
        productRepository.delete(p);
    }

    // ---------------- helpers ----------------

    private String currentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof AuthPrincipal ap)
            return ap.userId();
        return principal.toString();
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private void assertOwnerOrAdmin(Product p) {
        String userId = currentUserId();
        if (!isAdmin() && !p.getUserId().equals(userId)) {
            throw new ForbiddenException("You are not allowed to modify this product");
        }
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getUserId(),
                p.getCreatedAt());
    }
}
