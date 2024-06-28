package com.Backend.Back.service;

import com.Backend.Back.dao.ProductDao;
import com.Backend.Back.dao.UserProductDao;
import com.Backend.Back.entity.Product;
import com.Backend.Back.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
@Service
public class ProductService {
    @Autowired
    private ProductDao ProductDao;

    public Long saveProduct(String productName,String fileName, byte[] content, String category, int stock, String Price,
                               String description) {
        Date currentDate = new Date();
        Product Product = new Product();
        Product.setProductName(productName);
        Product.setFileName(fileName);
        Product.setContent(content);
        Product.setCategory(category);
        Product.setPrice(Price);
        Product.setStock(stock);
        Product.setStatus("activate");
        Product.setCreated_at(currentDate);
        Product.setDescription(description);


        Product savedEntity = ProductDao.save(Product);
        return savedEntity.getId();

    }
    @Autowired
    private UserProductDao userProductDao;

    public long countProductsById(Long productId) {
        return userProductDao.countByProductId(productId);
    }

    public Long countProducts() {
        return ProductDao.count();
    }
    public ResponseEntity<String> updateProductById(Long Id, Product updatedProduct) {
        Optional<Product> userOptional = ProductDao.findById(Id);


        if (userOptional.isPresent()) {
            Product Product = userOptional.get();

            if (updatedProduct.getProductName() != null && !updatedProduct.getProductName().isEmpty()) {
                Product.setProductName(updatedProduct.getProductName());
            }
            if (updatedProduct.getCategory() != null && !updatedProduct.getCategory().isEmpty()) {
                Product.setCategory(updatedProduct.getCategory());
            }
            if (updatedProduct.getContent() != null ) {
                Product.setContent(updatedProduct.getContent());
            }

            if (updatedProduct.getPrice() != null) {
                Product.setPrice(updatedProduct.getPrice());
            }

            if (updatedProduct.getStock() != -1) {
                Product.setStock(updatedProduct.getStock());
            }
            ProductDao.save(Product);


            return ResponseEntity.ok("User information updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
