package com.service.marketplace.dto.response;

import lombok.Data;

@Data
public class FilesResponse {
    private Integer id;
    private String url;
    private Integer serviceId;
    private Integer reviewId;
}
