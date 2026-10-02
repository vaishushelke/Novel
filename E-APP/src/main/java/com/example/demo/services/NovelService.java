package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Novel;
import com.example.demo.repos.NovelRepo;

@Service
public class NovelService {

    @Autowired
    private NovelRepo novelRepo;

    // Add Novel
    public Novel saveNovel(Novel novel) {

        return novelRepo.save(novel);
    }

    // Get All Novels
    public List<Novel> getAllNovels() {

        return novelRepo.findAll();
    }

     
    // Get Novel By ID
    public Novel getNovel(int id) {
        
        return novelRepo.findById(id).orElse(null);
    }

    // Update Novel
    public Novel updateNovel(int id, Novel novel) {
        
        Novel existingNovel = 
                novelRepo.findById(id).orElse(null);
                
        if (existingNovel == null) {
            return null;
        }
        
        existingNovel.setTitle(novel.getTitle());
        existingNovel.setAuthor(novel.getAuthor());
        existingNovel.setCategory(novel.getCategory());
        existingNovel.setPrice(novel.getPrice());
        
        return novelRepo.save(existingNovel);
    }

    // Delete Novel
    public boolean deleteNovel(int id) {
        if (!novelRepo.existsById(id)) {
            return false;
        }
        novelRepo.deleteById(id);
        
        return true;
    }

    // Search By Title
    public List<Novel> searchNovel(String title) {
        
        return novelRepo
                .findByTitleContainingIgnoreCase(title);
    }

    // Find By Author
    public List<Novel> getByAuthor(String author) {
        return novelRepo
                .findByAuthorIgnoreCase(author);
    }

    // Find By Category
    public List<Novel> getByCategory(String category) {
        return novelRepo
                .findByCategoryIgnoreCase(category);
    }

    // Find By Price
    public List<Novel> getByPrice(
            double min,
            double max) {
        return novelRepo
                .findByPriceBetween(min, max);
    }

}