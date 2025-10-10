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

    //stripe -API
    //-> productName , amount , quantity , currency
    //-> return sessionId and url


    public StripeResponse checkoutProducts(ProductRequest productRequest) {
        Stripe.apiKey = secretKey;

        // Add validation logging
//        System.out.println("Processing payment for: " + productRequest.getName());
//        System.out.println("Amount: " + productRequest.getAmount());
//        System.out.println("Quantity: " + productRequest.getQuantity());
//        System.out.println("Currency: " + productRequest.getCurrency());
//        System.out.println("Stripe API Key starts with: " + (secretKey != null ? secretKey.substring(0, 7) : "NULL"));

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

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:3000/bookings/payment/success")
                        .setCancelUrl("http://localhost:3000/bookings/payment/failure")
                        .addLineItem(lineItem)
                        .build();

        Session session = null;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            // THIS IS CRITICAL - Print the actual error
//            System.err.println("Stripe Error Code: " + e.getCode());
//            System.err.println("Stripe Error Message: " + e.getMessage());
//            System.err.println("Stripe Error Type: " + e.getClass().getName());
//            e.printStackTrace();

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

}