package com.example.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    private List<WeatherDescription> weather;

    public List<WeatherDescription> getWeather() {
        return weather;
    }

    public WeatherResponse() {
    }

    public WeatherResponse(List<WeatherDescription> weather) {
        this.weather = weather;
    }

    public void setWeather(List<WeatherDescription> weather) {
        this.weather = weather;
    }

}