package com.service.marketplace.service;

import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URL;

public interface StorageService {
    String uploadFile(MultipartFile file);

    URL generatePreSignedUrl(String bucketName, String fileName);

    boolean isPresignedUrlExpired(URL presignedUrl);

    String getPicture() throws MalformedURLException;

    String deleteFile(String fileName);
}

