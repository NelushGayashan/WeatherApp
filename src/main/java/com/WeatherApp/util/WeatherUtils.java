package com.WeatherApp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class WeatherUtils {

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    public static double formatTemperature(double temperature) {
        return BigDecimal.valueOf(temperature)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
