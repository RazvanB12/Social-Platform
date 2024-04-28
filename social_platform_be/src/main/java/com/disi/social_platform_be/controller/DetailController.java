package com.disi.social_platform_be.controller;


import com.disi.social_platform_be.dto.requests.BasicDetailDto;
import com.disi.social_platform_be.dto.responses.DetailDto;
import com.disi.social_platform_be.dto.responses.ProfilePictureDto;
import com.disi.social_platform_be.dto.responses.Response;
import com.disi.social_platform_be.service.IDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DetailController {

    private final IDetailService detailService;

    @PatchMapping("/detail/{userId}")
    public ResponseEntity<Response<String>> updateDetails(@PathVariable("userId") String userId, @RequestBody BasicDetailDto detailDto) {
        var response = detailService.updateDetail(userId, detailDto);
        return ResponseEntity.ok(new Response<>(response));
    }

    @GetMapping("/detail/{userId}")
    public ResponseEntity<Response<DetailDto>> getDetails(@PathVariable("userId") String userId) {
        var response = detailService.getDetail(userId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @PatchMapping("/profile-picture/{userId}")
    public ResponseEntity<Response<String>> updateProfilePicture(@PathVariable("userId") String userId, @RequestParam(name = "picture") MultipartFile file) {
        var response = detailService.updateProfilePicture(userId, file);
        return ResponseEntity.ok(new Response<>(response));
    }

    @GetMapping("/profile-picture/{userId}")
    public ResponseEntity<Response<ProfilePictureDto>> getProfilePicture(@PathVariable("userId") String userId) {
        var response = detailService.getProfilePicture(userId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @DeleteMapping("/detail/{userId}")
    public ResponseEntity<Response<String>> deleteDetails(@PathVariable("userId") String userId) {
        var response = detailService.deleteDetail(userId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @DeleteMapping("/profile-picture/{userId}")
    public ResponseEntity<Response<String>> deleteProfilePicture(@PathVariable("userId") String userId) {
        var response = detailService.deleteProfilePicture(userId);
        return ResponseEntity.ok(new Response<>(response));
    }
}
