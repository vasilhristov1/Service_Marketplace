package com.service.marketplace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class FilesRequest {
    private MultipartFile multipartFile;
    private Integer serviceId;
    private Integer reviewId;
}
