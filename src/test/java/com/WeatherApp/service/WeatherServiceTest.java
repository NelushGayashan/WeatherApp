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
    private RequestBodyUriSpec requestBodyUriSpec; // Mock WebClient.RequestBodyUriSpec

    @Mock
    private RequestBodySpec requestBodySpec; // Mock WebClient.RequestBodySpec

    @Mock
    private ResponseSpec responseSpec; // Mock WebClient.ResponseSpec

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testGetWeatherSummary_Success() {
        // Arrange
        String city = "London";
        String mockApiResponse = "{ \"list\": [{ \"dt_txt\": \"2024-11-20 12:00:00\", \"main\": {\"temp\": 290.0}}]}"; // Mocked response

        // Using doReturn for method chaining
        doReturn(requestBodyUriSpec).when(webClient).get(); // Mock webClient.get() to return requestBodyUriSpec
        doReturn(requestBodySpec).when(requestBodyUriSpec).uri(anyString()); // Mock uri() to return requestBodySpec
        doReturn(responseSpec).when(requestBodySpec).retrieve(); // Mock retrieve() to return responseSpec
        doReturn(Mono.just(mockApiResponse)).when(responseSpec).bodyToMono(String.class); // Mock bodyToMono() to return mockApiResponse

        // Act
        WeatherResponse weatherResponse = weatherService.getWeatherSummary(city).join();

        // Assert
        assertNotNull(weatherResponse);
        assertEquals(city, weatherResponse.getCity());
        assertTrue(weatherResponse.getAverageTemperature() > 0);
    }

    @Test
    public void testGetWeatherSummary_CityNotFound() {
        // Arrange
        String city = "InvalidCity";
        doReturn(requestBodyUriSpec).when(webClient).get();
        doReturn(requestBodySpec).when(requestBodyUriSpec).uri(anyString());
        doReturn(responseSpec).when(requestBodySpec).retrieve();
        doReturn(Mono.error(new CityNotFoundException("City not found"))).when(responseSpec).bodyToMono(String.class);

        // Act & Assert
        Exception exception = assertThrows(CityNotFoundException.class, () -> weatherService.getWeatherSummary(city).join());
        assertEquals("City not found", exception.getMessage());
    }

    @Test
    public void testGetWeatherSummary_ApiUnavailable() {
        // Arrange
        String city = "London";
        doReturn(requestBodyUriSpec).when(webClient).get();
        doReturn(requestBodySpec).when(requestBodyUriSpec).uri(anyString());
        doReturn(responseSpec).when(requestBodySpec).retrieve();
        doReturn(Mono.error(new ApiUnavailableException("API Unavailable"))).when(responseSpec).bodyToMono(String.class);

        // Act & Assert
        Exception exception = assertThrows(ApiUnavailableException.class, () -> weatherService.getWeatherSummary(city).join());
        assertEquals("API Unavailable", exception.getMessage());
    }
}
