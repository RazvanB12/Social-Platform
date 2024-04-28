package com.disi.social_platform_be.controller;

import com.disi.social_platform_be.dto.requests.UpdateImageDto;
import com.disi.social_platform_be.dto.responses.BasicImageDto;
import com.disi.social_platform_be.dto.responses.ImageDto;
import com.disi.social_platform_be.dto.responses.NewsFeedImage;
import com.disi.social_platform_be.dto.responses.Response;
import com.disi.social_platform_be.service.IImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ImageController {

    private final IImageService imageService;

    @PostMapping("/image/{userId}/{albumName}")
    public ResponseEntity<Response<String>> saveImage(@RequestParam(name = "image") MultipartFile file,
                                                      @PathVariable("userId") String userId, @PathVariable("albumName") String albumName) {
        var response = imageService.createImage(file, userId, albumName);
        return ResponseEntity.ok(new Response<>(response));
    }

    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<Response<String>> deleteImage(@PathVariable("imageId") String imageId) {
        var response = imageService.deleteImage(imageId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @PatchMapping("/image")
    public ResponseEntity<Response<String>> updateImagePrivacy(@Valid @RequestBody UpdateImageDto updateImageDto) {
        var response = imageService.updateImagePrivacy(updateImageDto);
        return ResponseEntity.ok(new Response<>(response));
    }

    @GetMapping("/image/{imageId}")
    public ResponseEntity<Response<ImageDto>> getImageById(@PathVariable("imageId") String imageId) {
        var response = imageService.getImageById(imageId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @PatchMapping("/image/report/{imageId}")
    public ResponseEntity<Response<String>> reportImage(@PathVariable("imageId") String imageId) {
        var response = imageService.reportImage(imageId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @DeleteMapping("/admin/image/{imageId}")
    public ResponseEntity<Response<String>> blockImage(@PathVariable("imageId") String imageId) {
        var response = imageService.blockImage(imageId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @PatchMapping("/admin/image/{imageId}")
    public ResponseEntity<Response<String>> rejectReportedImage(@PathVariable("imageId") String imageId) {
        var response = imageService.rejectReportedImage(imageId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @GetMapping("/admin/image")
    public ResponseEntity<Response<List<BasicImageDto>>> getReportedImages() {
        var response = imageService.getReportedImages();
        return ResponseEntity.ok(new Response<>(response));
    }

    @GetMapping("/newsFeed/{userId}")
    public ResponseEntity<Response<List<NewsFeedImage>>> getNewsFeed(@PathVariable("userId") String userId) {
        var response = imageService.getNewsFeed(userId);
        return ResponseEntity.ok(new Response<>(response));
    }
}
