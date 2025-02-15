package com.WeatherApp.controller;

import com.WeatherApp.exception.CityNotFoundException;
import com.WeatherApp.service.WeatherService;
import com.WeatherApp.model.WeatherResponse;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public CompletableFuture<WeatherResponse> getWeather(@RequestParam String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new CityNotFoundException("City name must not be empty.");
        }
        return weatherService.getWeatherSummary(city);
    }
}
