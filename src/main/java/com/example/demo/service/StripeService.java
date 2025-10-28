package com.example.demo.service;

import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

public StripeResponse checkoutProducts(ProductRequest productRequest, String bookingType) {
    Stripe.apiKey = secretKey;

    SessionCreateParams.LineItem.PriceData.ProductData productData =
            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .setName(productRequest.getName())
                    .build();

    SessionCreateParams.LineItem.PriceData priceData =
            SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency(productRequest.getCurrency() != null ? productRequest.getCurrency() : "inr")
                    .setUnitAmount(productRequest.getAmount())
                    .setProductData(productData)
                    .build();

    SessionCreateParams.LineItem lineItem =
            SessionCreateParams
                    .LineItem.builder()
                    .setQuantity(productRequest.getQuantity())
                    .setPriceData(priceData)
                    .build();

    // Set success and cancel URLs based on booking type
    String successUrl = bookingType != null && bookingType.equals("PACKAGE")
            ? "http://localhost:5173/bookings/packages/payment/success"
            : "http://localhost:5173/bookings/payment/success";

    String cancelUrl = bookingType != null && bookingType.equals("PACKAGE")
            ? "http://localhost:5173/bookings/packages/payment/failure"
            : "http://localhost:5173/bookings/payment/failure";

    SessionCreateParams params =
            SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addLineItem(lineItem)
                    .build();

    Session session = null;
    try {
        session = Session.create(params);
    } catch (StripeException e) {
        throw new RuntimeException("Failed to create Stripe session: " + e.getMessage(), e);
    }

    return StripeResponse
            .builder()
            .status("SUCCESS")
            .message("Payment session created")
            .sessionId(session.getId())
            .sessionUrl(session.getUrl())
            .build();
}

    // Keep the old method for backward compatibility
    public StripeResponse checkoutProducts(ProductRequest productRequest) {
        return checkoutProducts(productRequest, null);
    }

}