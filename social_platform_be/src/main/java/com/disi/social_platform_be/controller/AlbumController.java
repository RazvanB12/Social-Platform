package com.disi.social_platform_be.controller;

import com.disi.social_platform_be.dto.requests.BasicAlbumDto;
import com.disi.social_platform_be.dto.responses.AlbumDto;
import com.disi.social_platform_be.dto.responses.Response;
import com.disi.social_platform_be.service.IAlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AlbumController {

    private final IAlbumService albumService;

    @PostMapping("/album")
    public ResponseEntity<Response<String>> createAlbum(@Valid @RequestBody BasicAlbumDto basicAlbumDto) {
        var response = albumService.createAlbum(basicAlbumDto);
        return ResponseEntity.ok(new Response<>(response));
    }

    @DeleteMapping("/album")
    public ResponseEntity<Response<String>> removeAlbum(@Valid @RequestBody BasicAlbumDto basicAlbumDto) {
        var response = albumService.removeAlbum(basicAlbumDto);
        return ResponseEntity.ok(new Response<>(response));
    }

    @GetMapping("/album/{userId}")
    public ResponseEntity<Response<List<AlbumDto>>> getAllAlbumsByUserId(@PathVariable("userId") String userId) {
        var response = albumService.getAllAlbumsByUser(userId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @GetMapping("/album/{userId}/{albumName}")
    public ResponseEntity<Response<AlbumDto>> getAlbum(@PathVariable("userId") String userId, @PathVariable("albumName") String albumName) {
        var response = albumService.getAlbum(userId, albumName);
        return ResponseEntity.ok(new Response<>(response));
    }
}
