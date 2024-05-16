package com.example.weather.model;

import lombok.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@ConditionalOnProperty
@Table(name ="weather_data")
public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @NonNull
    private String city;

    @NonNull
    private String country;

    @NonNull
    private String description;

    public WeatherData() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WeatherData(@NonNull String city, @NonNull String country, @NonNull String description) {
        this.city = city;
        this.country = country;
        this.description = description;
    }

    public String getDescription(){
        System.out.println("this.description="+this.description);
        return this.description;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}
