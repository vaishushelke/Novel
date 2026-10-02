package com.example.demo.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Novel;
import com.example.demo.helpers.ApiResponse;
import com.example.demo.services.NovelService;

@RestController
@RequestMapping("/novel")
@CrossOrigin("http://localhost:5173")
public class NovelController {

	@Autowired
	private NovelService nser;
	
	
	@PostMapping("/save")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Novel>> saveNovel(@RequestBody Novel novel){
		Novel savedNovel=nser.saveNovel(novel);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true,"Novel added sucessfully",savedNovel)
						);
	}
	
	
	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<Novel>>> getAllNovels(){
		List<Novel> novels=nser.getAllNovels();
		return ResponseEntity.ok(new ApiResponse<>(
				true,
				"Novel fetched successfully",
				novels
				)
			);
	}
	
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Novel>> getNovel(
            @PathVariable int id) {
        
        Novel novel = nser.getNovel(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Novel fetched successfully",
                        novel
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteNovel(
            @PathVariable int id) {
        
        nser.deleteNovel(id);
        
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Novel deleted successfully",
                        null
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Novel>>> searchNovel(
            @RequestParam String title) {
        
        List<Novel> novels = nser.searchNovel(title);
        
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Search results fetched successfully",
                        novels
                )
        );
    }

    @GetMapping("/category")
    public ResponseEntity<ApiResponse<List<Novel>>> getByCategory(
            @RequestParam String category) {
        
        List<Novel> novels = nser.getByCategory(category);
        
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Novels fetched by category successfully",
                        novels
                )
        );
    }

    @GetMapping("/author")
    public ResponseEntity<ApiResponse<List<Novel>>> getByAuthor(
            @RequestParam String author) {
        
        List<Novel> novels = nser.getByAuthor(author);
        
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Novels fetched by author successfully",
                        novels
                )
        );
    }

	
}