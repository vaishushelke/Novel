package com.example.demo.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Novel;

@Repository
public interface NovelRepo extends JpaRepository<Novel, Integer>{

    List<Novel> findByPriceBetween(double minPrice, double maxPrice);

    List<Novel> findByTitleContainingIgnoreCase(String title);

    List<Novel> findByAuthor(String author);

    List<Novel> findByCategory(String category);

    List<Novel> findByAuthorIgnoreCase(String author);

    List<Novel> findByCategoryIgnoreCase(String category);

}