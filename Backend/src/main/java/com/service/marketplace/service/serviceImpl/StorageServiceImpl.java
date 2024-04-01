package com.service.marketplace.service.serviceImpl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.StorageService;
import com.service.marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageServiceImpl implements StorageService {

    private final AmazonS3 s3Client;
    private final UserRepository userRepository;
    private final UserService userService;
    @Value("${cloud.aws.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.presigned-url.expiration}")
    private long expirationPresigned;

    public String uploadFile(MultipartFile file) {
        if (file == null) {
            return null;
        }
        File fileObj = convertMultipartFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        System.out.println(bucketName);

        URL preSignedUrl = generatePreSignedUrl(bucketName, fileName);
        int userId = userService.getCurrentUser().getId();

        User user = userRepository.findById(userId).orElse(null);
        user.setPicture(String.valueOf(preSignedUrl));
        user.setMediaKey(fileName);
        userRepository.save(user);

        fileObj.delete();

        return preSignedUrl.toString();
    }

    public URL generatePreSignedUrl(String bucketName, String fileName) {
        Date expiration = new Date(System.currentTimeMillis() + expirationPresigned);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        return s3Client.generatePresignedUrl(generatePresignedUrlRequest);

    }

    public boolean isPresignedUrlExpired(URL presignedUrl) {
        Date expirationTime = new Date(presignedUrl.getQuery().contains("X-Amz-Expires=") ?
                Long.parseLong(presignedUrl.getQuery().split("X-Amz-Expires=")[1].split("&")[0]) * 1000 :
                0);
        return expirationTime.before(new Date());
    }

    public String getPicture() throws MalformedURLException {
        int userId = userService.getCurrentUser().getId();
        User user = userRepository.findById(userId).orElse(null);
        URL presignedUrl = user.getPicture() != null ? new URL(user.getPicture()) : null;
        if (presignedUrl != null && isPresignedUrlExpired(presignedUrl)) {
            String mediaKey = user.getMediaKey();
            URL newPresignedUrl = generatePreSignedUrl(bucketName, mediaKey);
            user.setPicture(String.valueOf(newPresignedUrl));
            userRepository.save(user);
            return newPresignedUrl.toString();
        } else {
            return presignedUrl.toString();
        }
    }

    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed...";
    }

    private File convertMultipartFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}