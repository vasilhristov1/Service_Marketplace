package com.service.marketplace.controller;

import com.service.marketplace.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cloudinary")
public class CloudinaryController {
    private final CloudinaryService cloudinaryService;

    @PutMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return new ResponseEntity<>(cloudinaryService.uploadFile(multipartFile), HttpStatus.OK);
    }

    @GetMapping("/allPictures")
    public ResponseEntity<List<String>> getAllPictures() throws IOException {
        return new ResponseEntity<>(cloudinaryService.getAllPictures(), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("url") String url) {
        return new ResponseEntity<>(cloudinaryService.deleteFile(url), HttpStatus.OK);
    }
}
