package com.example.weather.controller;

import com.example.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidRequest() {
        when(weatherService.getWeatherDetail("London", "UK","valid_api_key")).thenReturn("Sunny");

        ResponseEntity<?> response = weatherController.getWeather("London", "UK", "valid_api_key");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sunny", response.getBody());
    }

    @Test
    void testInvalidApiKey() {
        ResponseEntity<?> response = weatherController.getWeather("London", "UK", "");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid API Key", response.getBody());
    }

    @Test
    void testRateLimitExceeded() {
        when(weatherService.getWeatherDetail("London", "UK","valid_api_key")).thenReturn("Sunny");

        for (int i = 0; i < 6; i++) {
            ResponseEntity<?> response = weatherController.getWeather("London", "UK", "valid_api_key");
        }

        ResponseEntity<?> response = weatherController.getWeather("London", "UK", "valid_api_key");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Rate Limit Exceeded. Please try again after an hour", response.getBody());
    }

    @Test
    void testInvalidCityOrCountry() {
        ResponseEntity<?> response = weatherController.getWeather("abc09", "", "valid_api_key");

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid City or Country Name", response.getBody());
    }
}
