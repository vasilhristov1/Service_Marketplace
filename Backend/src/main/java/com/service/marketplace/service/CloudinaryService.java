package com.service.marketplace.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CloudinaryService {
    String uploadFile(MultipartFile multipartFile) throws IOException;

    String deleteFile(String pictureUrl);

    List<String> getAllPictures();

}
