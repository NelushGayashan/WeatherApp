package com.WeatherApp.service;

import com.WeatherApp.exception.CityNotFoundException;
import com.WeatherApp.exception.ApiUnavailableException;
import com.WeatherApp.mapper.WeatherMapper;
import com.WeatherApp.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("ALL")
@Service
public class WeatherService {

    private final WebClient webClient;
    private final String apiKey;

    public WeatherService(WebClient.Builder webClientBuilder, @Value("${weather.api.key}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl("https://api.openweathermap.org/data/2.5/forecast").build();
        this.apiKey = apiKey;
    }

    @Cacheable(value = "weatherCache", key = "#city", unless = "#result == null")
    public CompletableFuture<WeatherResponse> getWeatherSummary(String city) {
        return fetchWeatherData(city)
                .thenApply(data -> WeatherMapper.mapToWeatherResponse(city, (List<Map<String, Object>>) data.get("list")));
    }

    @Async
    private CompletableFuture<Map> fetchWeatherData(String city) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .onStatus(status -> status.value() == 401, response ->
                        Mono.error(new ApiUnavailableException("Invalid API key provided."))
                )
                .onStatus(status -> status.value() == 404, response ->
                        Mono.error(new CityNotFoundException("City not found: " + city))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new ApiUnavailableException("External API is unavailable."))
                )
                .bodyToMono(Map.class)
                .toFuture();
    }
}
