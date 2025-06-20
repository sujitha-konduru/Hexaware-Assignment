package com.example.demo.controller;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/products")
public class ProductController {
  @Autowired ProductService service;

  @Operation(summary="Add product")
  @PostMapping public Product add(@RequestBody Product p){return service.addProduct(p);}
  @Operation(summary="Get all products")
  @GetMapping public List<Product> all(){ return service.getAllProducts(); }
  @Operation(summary="Get product by ID")
  @GetMapping("/{id}") public Product get(@PathVariable Long id){ return service.getProductById(id); }
  @Operation(summary="Update product")
  @PutMapping("/{id}") public Product upd(@PathVariable Long id, @RequestBody Product p){
    return service.updateProduct(id,p);
  }
  @Operation(summary="Delete product")
  @DeleteMapping("/{id}") public void del(@PathVariable Long id){ service.deleteProduct(id);}
}
