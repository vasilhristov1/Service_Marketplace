package com.service.marketplace.controller;

import com.service.marketplace.dto.response.FilesResponse;
import com.service.marketplace.service.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FilesController {
    private final FilesService filesService;

    @GetMapping("/all")
    public ResponseEntity<List<FilesResponse>> getAllFiles() {
        List<FilesResponse> files = filesService.getAllFiles();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<FilesResponse> getFileById(@PathVariable("fileId") Integer fileId) {
        FilesResponse file = filesService.getFileById(fileId);
        if (file != null) {
            return ResponseEntity.ok(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<FilesResponse>> getFilesByServiceId(@PathVariable("serviceId") Integer serviceId) {
        List<FilesResponse> serviceFiles = filesService.getFilesByServiceId(serviceId);
        return ResponseEntity.ok(serviceFiles);
    }

    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<FilesResponse>> getFilesByReviewId(@PathVariable("reviewId") Integer reviewId) {
        List<FilesResponse> reviewFiles = filesService.getFilesByReviewId(reviewId);
        return ResponseEntity.ok(reviewFiles);
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable("fileId") Integer fileId) {
        filesService.deleteFileById(fileId);
        return ResponseEntity.ok().build();
    }
}
