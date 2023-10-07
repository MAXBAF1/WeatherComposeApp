package com.example.weathercomposeapp

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weathercomposeapp.data.WeatherModel
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.roundToInt

const val API_KEY = "32c66a5d22264c7eb41102922230610"

class WeatherApi {
    fun getWeather(
        context: Context, city: String, callback: (List<WeatherModel>) -> Unit
    ) {
        val url =
            "https://api.weatherapi.com/v1/forecast.json?key=$API_KEY&q=$city&days=3&aqi=no&alerts=no"
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(Request.Method.GET, url, { response ->
            val list = getWeatherByDays(response)
            callback(list)
        }, { error ->
            Log.e("MyLog", error.toString())
        })
        queue.add(request)
    }

    private fun getWeatherByDays(response: String): List<WeatherModel> {
        if (response.isEmpty()) return listOf()
        val list = ArrayList<WeatherModel>()
        val mainObject = JSONObject(response)
        val city = mainObject.getJSONObject("location").getString("name")
        val days = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
        for (i in 0 until days.length()) {
            val item = days[i] as JSONObject
            val day = item.getJSONObject("day")
            list.add(
                WeatherModel(
                    city,
                    item.getString("date"),
                    "",
                    day.getJSONObject("condition").getString("text"),
                    day.getJSONObject("condition").getString("icon"),
                    day.getString("maxtemp_c").toFloat().roundToInt().toString(),
                    day.getString("mintemp_c").toFloat().roundToInt().toString(),
                    getWeatherByHours(item.getJSONArray("hour"))
                )
            )
        }
        val current = mainObject.getJSONObject("current")
        list[0] = list[0].copy(
            time = current.getString("last_updated"),
            currentTemp = current.getString("temp_c").toFloat().roundToInt().toString()
        )
        return list
    }

    private fun getWeatherByHours(hoursArray: JSONArray): List<WeatherModel> {
        if (hoursArray.toString().isEmpty()) return emptyList()
        val list = ArrayList<WeatherModel>()
        for (i in 0 until hoursArray.length()) {
            val item = hoursArray[i] as JSONObject
            list.add(
                WeatherModel(
                    "",
                    item.getString("time"),
                    item.getString("temp_c").toFloat().roundToInt().toString(),
                    item.getJSONObject("condition").getString("text"),
                    item.getJSONObject("condition").getString("icon"),
                    "",
                    "",
                    emptyList()
                )
            )
        }

        return list
    }
}