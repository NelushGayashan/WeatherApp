package com.WeatherApp.mapper;

import com.WeatherApp.model.WeatherResponse;
import com.WeatherApp.util.WeatherUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("ALL")
public class WeatherMapper {

    private static final SimpleDateFormat ORIGINAL_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat TARGET_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static WeatherResponse mapToWeatherResponse(String city, List<Map<String, Object>> forecastList) {
        List<WeatherData> weatherDataList = new ArrayList<>();

        for (Map<String, Object> item : forecastList) {
            Map<String, Object> main = (Map<String, Object>) item.get("main");
            String date = (String) item.get("dt_txt");

            double tempKelvin = ((Number) main.get("temp")).doubleValue(); // Convert to double
            double tempCelsius = WeatherUtils.kelvinToCelsius(tempKelvin);

            weatherDataList.add(new WeatherData(parseDate(date), tempCelsius));
        }

        double avgTemp = WeatherUtils.formatTemperature(
                weatherDataList.stream()
                        .mapToDouble(WeatherData::temperature)
                        .average()
                        .orElse(0.0)
        );

        WeatherData hottest = Collections.max(weatherDataList, Comparator.comparingDouble(WeatherData::temperature));
        WeatherData coldest = Collections.min(weatherDataList, Comparator.comparingDouble(WeatherData::temperature));

        return new WeatherResponse(city, avgTemp, hottest.date(), coldest.date());
    }

    private static String parseDate(String dateStr) {
        try {
            return TARGET_FORMAT.format(ORIGINAL_FORMAT.parse(dateStr));
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date", e);
        }
    }

    private record WeatherData(String date, double temperature) {}
}
