package com.service.marketplace.service;

import com.service.marketplace.dto.request.Checkout;
import com.service.marketplace.dto.request.OfferPaymentRequest;
import com.service.marketplace.dto.request.StripeAccountRequest;
import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;

public interface StripeService {
    String createStripeAccount(StripeAccountRequest stripeAccountRequest);

    String subscriptionWithCheckoutPage(Checkout checkout) throws StripeException;

    String getProductPrice(String priceId);

    ResponseEntity<String> handleStripeWebhook(String request, String payload);

    ResponseEntity<String> cancelSubscription(String subscriptionId);

    String payCheckout(OfferPaymentRequest offerPaymentRequest) throws StripeException;


    String vipWithCheckoutPage(Checkout checkout) throws StripeException;
}
