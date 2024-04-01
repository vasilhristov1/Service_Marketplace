package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.VipServiceRequest;
import com.service.marketplace.dto.response.VipServiceResponse;
import com.service.marketplace.persistence.entity.VipService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VipServiceMapper {

    VipService vipServiceRequestToVipService(VipServiceRequest request);

    @Mapping(target = "serviceId", source = "service.id")
    VipServiceResponse toVipServiceResponse(VipService vipService);

    List<VipServiceResponse> toVipServiceResponseList(List<VipService> vipServices);

    VipServiceResponse getVipServiceByUserId(Integer userId);

}
