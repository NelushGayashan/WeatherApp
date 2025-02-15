package com.WeatherApp.controller;

import com.WeatherApp.service.WeatherService;
import com.WeatherApp.model.WeatherResponse;
import com.WeatherApp.exception.CityNotFoundException;
import com.WeatherApp.exception.ApiUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class WeatherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    @Test
    public void testGetWeather_Success() throws Exception {
        // Arrange
        String city = "London";
        WeatherResponse mockResponse = new WeatherResponse(city, 15.5, "2024-11-20", "2024-11-18");
        when(weatherService.getWeatherSummary(city)).thenReturn(java.util.concurrent.CompletableFuture.completedFuture(mockResponse));

        // Act & Assert
        mockMvc.perform(get("/weather?city=London"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value(city))
                .andExpect(jsonPath("$.averageTemperature").value(15.5))
                .andExpect(jsonPath("$.hottestDay").value("2024-11-20"))
                .andExpect(jsonPath("$.coldestDay").value("2024-11-18"));
    }

    @Test
    public void testGetWeather_CityNotFound() throws Exception {
        // Arrange
        String city = "InvalidCity";
        when(weatherService.getWeatherSummary(city)).thenThrow(new CityNotFoundException("City not found"));

        // Act & Assert
        mockMvc.perform(get("/weather?city=InvalidCity"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetWeather_ApiUnavailable() throws Exception {
        // Arrange
        String city = "London";
        when(weatherService.getWeatherSummary(city)).thenThrow(new ApiUnavailableException("API Unavailable"));

        // Act & Assert
        mockMvc.perform(get("/weather?city=London"))
                .andExpect(status().isServiceUnavailable());
    }
}
