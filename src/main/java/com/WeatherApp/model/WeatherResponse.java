package com.WeatherApp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeatherResponse {
    private String city;
    private double averageTemperature;
    private String hottestDay;
    private String coldestDay;
}
