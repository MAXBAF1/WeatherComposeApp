package com.example.weathercomposeapp.data

data class WeatherModel(
    val city: String,
    val time: String,
    val currentTemp: String,
    val condition: String,
    val icon: String,
    val maxTemp: String,
    val minTemp: String,
    var hours: List<WeatherModel>
) {
    constructor() : this("", "", "", "", "", "", "", emptyList())
}
