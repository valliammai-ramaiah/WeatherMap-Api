package com.example.weather.service;

import com.example.weather.model.WeatherData;
import com.example.weather.model.WeatherResponse;
import com.example.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private RestTemplate restTemplate;

    public String getWeatherDetail(String city, String country, String apiKey){
        String weatherDescription = getWeatherDescription(city,country);
        if(weatherDescription != null)
            return weatherDescription;
        else {
            getWeatherDataFromAPI(city, country, apiKey);
            return getWeatherDescription(city,country);
        }
    }

    public String getWeatherDescription(String city, String country) {
        try {
            WeatherData weatherData = weatherRepository.findByCityAndCountry(city, country);
            if(weatherData != null) {
                System.out.println(weatherData.toString());
                return weatherData.getDescription();
            }
        } catch (DataAccessException e) {
            System.err.println("Error occurred while retrieving weather data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
        return null;
    }

    public void getWeatherDataFromAPI(String city, String country, String apiKey) {
        String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&appid=" + apiKey;
        try {
            WeatherResponse weatherResponse = restTemplate.getForObject(apiUrl, WeatherResponse.class);
            if (weatherResponse != null && !weatherResponse.getWeather().isEmpty()) {
                String description = weatherResponse.getWeather().get(0).getDescription();
                saveWeatherData(city, country, description);
            } else {
                throw new RuntimeException("Failed to retrieve weather data from OpenWeatherMap API.");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error occurred while retrieving weather data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void saveWeatherData(String city, String country, String description) {
        WeatherData weatherData = new WeatherData(city, country, description);
        try {
            weatherRepository.save(weatherData);
            System.out.println("Weather data saved successfully for city:" + city+ "country:" +country+ "description:"+description);
        } catch (DataAccessException e) {
            System.err.println("Error occurred while saving weather data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
