package com.example.demo.service.imp;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ProductServiceImpl implements ProductService {
  @Autowired ProductRepository repo;
  @Override public Product addProduct(Product p){return repo.save(p);}
  @Override public List<Product> getAllProducts(){return repo.findAll();}
  @Override public Product getProductById(Long id){
    return repo.findById(id).orElseThrow(()->new ProductNotFoundException("Not found"));
  }
  @Override public Product updateProduct(Long id, Product p){
    Product e = getProductById(id);
    e.setPname(p.getPname()); e.setPrice(p.getPrice()); e.setReview(p.getReview());
    return repo.save(e);
  }
  @Override public void deleteProduct(Long id){
    getProductById(id);
    repo.deleteById(id);
  }
}



