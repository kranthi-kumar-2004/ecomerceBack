package com.student.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.management.Repository.ProductRepository;
import com.student.management.enitity.Products;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public List<Products> getAll() {
        return repo.findAll();
    }

    public Products save(Products p) {
        return repo.save(p);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Products update(Long id, Products p) {
    Products existing = repo.findById(id).orElse(null);

    if (existing == null) {
        System.out.println("ID NOT FOUND: " + id);
        return null; // or throw custom error
    }

    existing.setName(p.getName());
    existing.setPrice(p.getPrice());
    existing.setImage(p.getImage());
    existing.setCategory(p.getCategory());
    existing.setStock(p.getStock());
    existing.setDescription(p.getDescription());

    return repo.save(existing);
}
}