package com.example.weather.service;

import com.example.weather.model.WeatherData;
import com.example.weather.model.WeatherDescription;
import com.example.weather.model.WeatherResponse;
import com.example.weather.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class WeatherServiceTest {

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWeatherDescription_DataInRepository() {
        WeatherData weatherData = new WeatherData();
        weatherData.setCity("London");
        weatherData.setCountry("UK");
        weatherData.setDescription("Sunny");
        when(weatherRepository.findByCityAndCountry("London", "UK")).thenReturn(weatherData);

        String weatherDescript = weatherService.getWeatherDescription("London", "UK");

        verify(weatherRepository, times(1)).findByCityAndCountry("London", "UK");

        assertEquals("Sunny", weatherDescript);
    }

    @Test
    void testGetWeatherDescription_DataNotInRepository() {
            when(weatherRepository.findByCityAndCountry(anyString(), anyString())).thenReturn(null);

            String result = weatherService.getWeatherDescription("NonExistingCity", "NonExistingCountry");

            assertNull(result, "Expected null result when data is not found in repository");
        }

    @Test
    void testSaveWeatherData() {
        weatherService.saveWeatherData("New York", "US", "Very Hot");

        verify(weatherRepository, times(1)).save(any(WeatherData.class));
    }

}