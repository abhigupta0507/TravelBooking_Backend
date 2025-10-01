package com.example.demo.dto;

/**
 * Represents the status of a travel package.
 * - ACTIVE: The package is currently available for booking and the tour is running.
 * - UPCOMING: The package is scheduled for a future date and is available for booking.
 * - FINISHED: The package tour dates are in the past.
 */
public enum PackageStatus {
    ACTIVE,
    UPCOMING,
    FINISHED
}
