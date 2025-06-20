package com.example.demo.service;
import java.util.List;

import com.example.demo.model.Product;
public interface ProductService {
	  Product addProduct(Product p);
	  List<Product> getAllProducts();
	  Product getProductById(Long id);
	  Product updateProduct(Long id, Product p);
	  void deleteProduct(Long id);
	}

