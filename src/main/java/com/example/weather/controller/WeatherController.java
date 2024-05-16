package com.example.weather.controller;

import com.example.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private static final int MAX_REQUESTS_PER_HOUR = 5;
    private Map<String, AtomicInteger> apiKeyRequestCount = new ConcurrentHashMap<>();

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{city}/{country}")
    public ResponseEntity<?> getWeather(@PathVariable String city,
                                        @PathVariable String country,
                                        @RequestHeader("X-API-Key") String apiKey) {
        if (!isValidApiKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
        }

        if  (isRateLimitExceeded(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Rate Limit Exceeded. Please try again after an hour");
        }

        if (!isValidCityOrCountry(city,country)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid City or Country Name");
        }

        String weatherDescription = weatherService.getWeatherDetail(city, country, apiKey);
        if (weatherDescription != null) {
            incrementRequestCount(apiKey);
            return ResponseEntity.ok(weatherDescription);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Weather data not found");
        }
    }

    private boolean isValidApiKey(String apiKey) {
        return apiKey != null && !apiKey.isEmpty();
    }

    private boolean isRateLimitExceeded(String apiKey) {
        apiKeyRequestCount.putIfAbsent(apiKey, new AtomicInteger(0));
        return apiKeyRequestCount.get(apiKey).get() >= MAX_REQUESTS_PER_HOUR;
    }

    private boolean isValidCityOrCountry(String city, String country) {
        if (city == null || country == null || city.isEmpty() || country.isEmpty() ||
                !city.chars().allMatch(Character::isLetter) || !country.chars().allMatch(Character::isLetter)) {
            return false;
        }
        return true;
    }

    private void incrementRequestCount(String apiKey) {
        apiKeyRequestCount.computeIfPresent(apiKey, (key, value) -> {
            value.incrementAndGet();
            return value;
        });
    }
}