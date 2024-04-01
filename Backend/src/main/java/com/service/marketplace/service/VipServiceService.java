package com.service.marketplace.service;

import com.service.marketplace.dto.response.VipServiceResponse;

import java.util.List;

public interface VipServiceService {
    List<VipServiceResponse> getAllVipServices();

    VipServiceResponse getVipServiceById(Integer vipServiceId);

    void deleteVipServiceById(Integer vipServiceId);


}

