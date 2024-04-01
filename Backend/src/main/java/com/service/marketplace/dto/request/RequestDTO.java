package com.service.marketplace.dto.request;

import com.stripe.model.Product;
import lombok.Data;

@Data
public class RequestDTO {
    Product subscription;
    String customerName;
    String customerEmail;
}
