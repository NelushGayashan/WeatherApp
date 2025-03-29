# WeatherApp

## Overview
WeatherApp is a Spring Boot application that fetches, processes, and analyzes weather data asynchronously. It retrieves weather forecasts from an external API (OpenWeatherMap) and provides a summary, including the average temperature, hottest day, and coldest day for the past 7 days.

## Features
- Fetches weather data using WebClient.
- Processes and analyzes weather data.
- Computes the average temperature, hottest day, and coldest day.
- Caches results for 30 minutes using Caffeine Cache.
- Handles API failures and invalid city names gracefully.
- Implements asynchronous execution with `@Async` and `CompletableFuture`.
- Includes unit tests for service and controller layers using JUnit and Mockito.

## Technologies Used
- **Java 17**
- **Spring Boot 3+**
- **Spring Web**
- **Spring Cache (Caffeine)**
- **Spring WebFlux (for WebClient)**
- **Spring Boot Starter Test**
- **Lombok (for reducing boilerplate code)**
- **Mockito & JUnit (for unit testing)**

## Architecture Overview
The application follows a layered architecture:

- **Controller Layer**: Handles incoming HTTP requests.
- **Service Layer**: Fetches, processes, and caches weather data.
- **Exception Handling**: Provides robust error handling for API failures and invalid inputs.
- **Caching Layer**: Uses Caffeine Cache to store results for 30 minutes.
- **Asynchronous Execution**: Uses `@Async` and `CompletableFuture` to perform non-blocking API calls.

## Requirements

### Input
A city name provided as a query parameter to the endpoint:
```bash
GET /weather?city=London
```

### Output
A JSON response summarizing the weather for the given city:

```json
{
  "city": "London",
  "averageTemperature": 15.5,
  "hottestDay": "2024-11-20",
  "coldestDay": "2024-11-18"
}
```

## Technologies Used
- **Java 17**
- **Spring Boot 3+**
- **Spring Web**
- **Spring Cache (Caffeine)**
- **Spring WebFlux (for WebClient)**
- **Spring Boot Starter Test**
- **Lombok** (for reducing boilerplate code)
- **Mockito & JUnit** (for unit testing)

## Setup & Installation

### Prerequisites
Ensure you have the following installed:
- **Java 17+**
- **Maven 3+**
- **An OpenWeatherMap API key**

### Steps to Run
1. **Clone the repository**:
```bash
git clone https://github.com/NelushGayashan/WeatherApp.git
cd WeatherApp
```
2. **Configure your API key in application.properties**:
```bash
weather.api.key=your_openweathermap_api_key
```
3. **Build and run the application**:
```bash
mvn clean install
mvn spring-boot:run
```
4. **Access the API**:
```bash
http://localhost:8080/weather?city=London
```
## Caching Strategy
- Uses **Spring Cache** with **Caffeine**.
- **Cache timeout:** 30 minutes.
- **Cache key:** City name.

## Error Handling

| Error Type             | HTTP Status | Description                                      |
|------------------------|------------|--------------------------------------------------|
| Invalid City Name      | 404        | The specified city was not found.               |
| API Key Invalid/Missing | 401        | The provided API key is incorrect.              |
| External API Failure   | 503        | OpenWeatherMap API is unavailable.              |
| Generic Error         | 500        | An unexpected error occurred.                   |

## Running Tests
Run unit tests using:
```bash
mvn test
```

## Sample API Calls & Responses

### Successful Response  
**Request:**  
```bash
GET /weather?city=London
```
## Response

### Successful Response  
**Response:**  
```json
{
  "city": "London",
  "averageTemperature": 15.5,
  "hottestDay": "2024-11-20",
  "coldestDay": "2024-11-18"
}
```
### Error Response (Invalid City)  
**Request:**  
```bash
GET /weather?city=InvalidCity
```
**Response:**
```json
{
  "error": "City not found: InvalidCity"
}
```
## License
This project is licensed under the **MIT License**.

