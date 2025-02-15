package com.WeatherApp.service;

import com.WeatherApp.exception.ApiUnavailableException;
import com.WeatherApp.exception.CityNotFoundException;
import com.WeatherApp.model.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RequestBodySpec requestBodySpec;

    @Mock
    private ResponseSpec responseSpec;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWeatherSummary_Success() {
        String city = "London";
        String mockApiResponse = "{ \"list\": [{ \"dt_txt\": \"2024-11-20 12:00:00\", \"main\": {\"temp\": 290.0}}]}";

        doReturn(requestBodyUriSpec).when(webClient).get();
        doReturn(requestBodySpec).when(requestBodyUriSpec).uri(anyString());
        doReturn(responseSpec).when(requestBodySpec).retrieve();
        doReturn(Mono.just(mockApiResponse)).when(responseSpec).bodyToMono(String.class);

        WeatherResponse weatherResponse = weatherService.getWeatherSummary(city).join();

        assertNotNull(weatherResponse);
        assertEquals(city, weatherResponse.getCity());
        assertTrue(weatherResponse.getAverageTemperature() > 0);
    }

    @Test
    public void testGetWeatherSummary_CityNotFound() {
        String city = "InvalidCity";
        doReturn(requestBodyUriSpec).when(webClient).get();
        doReturn(requestBodySpec).when(requestBodyUriSpec).uri(anyString());
        doReturn(responseSpec).when(requestBodySpec).retrieve();
        doReturn(Mono.error(new CityNotFoundException("City not found"))).when(responseSpec).bodyToMono(String.class);

        Exception exception = assertThrows(CityNotFoundException.class, () -> weatherService.getWeatherSummary(city).join());
        assertEquals("City not found", exception.getMessage());
    }

    @Test
    public void testGetWeatherSummary_ApiUnavailable() {
        String city = "London";
        doReturn(requestBodyUriSpec).when(webClient).get();
        doReturn(requestBodySpec).when(requestBodyUriSpec).uri(anyString());
        doReturn(responseSpec).when(requestBodySpec).retrieve();
        doReturn(Mono.error(new ApiUnavailableException("API Unavailable"))).when(responseSpec).bodyToMono(String.class);

        Exception exception = assertThrows(ApiUnavailableException.class, () -> weatherService.getWeatherSummary(city).join());
        assertEquals("API Unavailable", exception.getMessage());
    }
}
