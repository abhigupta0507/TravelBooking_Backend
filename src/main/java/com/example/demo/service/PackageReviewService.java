package com.example.demo.service;

import com.example.demo.dao.PackageReviewDAO;
import com.example.demo.model.PackageReview;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PackageReviewService {

    private final PackageReviewDAO reviewDAO;

    public PackageReviewService(PackageReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    public void addReview(PackageReview review) {
        reviewDAO.addReview(review);
    }

    public List<PackageReview> getReviewsByPackageId(Integer packageId) {
        return reviewDAO.getReviewsByPackageId(packageId);
    }

    public List<PackageReview> getReviewsByUserId(Integer userId) {
        return reviewDAO.getReviewsByUserId(userId);
    }

    public void deleteReview(Integer reviewId) {
        reviewDAO.deleteReview(reviewId);
    }

    public Integer getCustomerIdByBookingId(Integer packageBookingId) {
        return reviewDAO.getUserIdByPackageBookingId(packageBookingId);
    }

    public boolean doesReviewExist(Integer reviewId) {
        return reviewDAO.doesReviewExist(reviewId);
    }

    public String getPackageBookingStatus(int packageId) {
        return reviewDAO.getPackageBookingStatus(packageId);
    }

    public int getCustomerIdByReviewId(Integer reviewId) {
        return reviewDAO.getCustomerIdByReviewId(reviewId);
    }
}
