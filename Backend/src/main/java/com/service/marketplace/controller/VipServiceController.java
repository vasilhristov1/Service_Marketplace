package com.service.marketplace.controller;

import com.service.marketplace.dto.response.VipServiceResponse;
import com.service.marketplace.service.VipServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vip-services")
public class VipServiceController {
    private final VipServiceService vipServiceService;

    @GetMapping("/all")
    public ResponseEntity<List<VipServiceResponse>> getAllVipServices() {
        List<VipServiceResponse> vipServices = vipServiceService.getAllVipServices();
        return ResponseEntity.ok(vipServices);
    }

    @GetMapping("/{vipServiceId}")
    public ResponseEntity<VipServiceResponse> getVipServiceById(@PathVariable("vipServiceId") Integer vipServiceId) {
        VipServiceResponse vipService = vipServiceService.getVipServiceById(vipServiceId);

        if (vipService != null) {
            return ResponseEntity.ok(vipService);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{vipServiceId}")
    public ResponseEntity<Void> deleteVipService(@PathVariable("vipServiceId") Integer vipServiceId) {
        vipServiceService.deleteVipServiceById(vipServiceId);
        return ResponseEntity.ok().build();
    }
}
