package com.ecommerce.Product.controller;

import com.ecommerce.Product.dto.ProductRequest;
import com.ecommerce.Product.dto.ProductResponse;
import com.ecommerce.Product.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return new ResponseEntity<ProductResponse>(productService.createProduct(productRequest),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProduct() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted=productService.deleteProduct(id);
        return deleted? ResponseEntity.noContent().build():ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProduct(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProduct(keyword));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> udateProduct(@RequestBody ProductRequest productRequest, @PathVariable Long id) {
        return productService.updateProduct(productRequest,id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }
}
