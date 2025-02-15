package com.WeatherApp.exception;

public class ApiUnavailableException extends RuntimeException {
    public ApiUnavailableException(String message) {
        super(message);
    }
}
