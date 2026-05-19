package com.mukesh.restaurantManagementSystem.exceptions;

public class SessionExpirationException extends RuntimeException {
    public SessionExpirationException(String message) {
        super(message);
    }
}
