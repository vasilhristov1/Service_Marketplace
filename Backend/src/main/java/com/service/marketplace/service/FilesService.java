package com.service.marketplace.service;

import com.service.marketplace.dto.request.FilesRequest;
import com.service.marketplace.dto.response.FilesResponse;

import java.util.List;

public interface FilesService {
    List<FilesResponse> getAllFiles();

    FilesResponse getFileById(Integer fileId);

    FilesResponse createFile(FilesRequest fileToCreate);

    void deleteFileById(Integer fileID);

    List<FilesResponse> getFilesByServiceId(Integer serviceId);

    List<FilesResponse> getFilesByReviewId(Integer reviewId);
}
