package com.example.weathercomposeapp

import android.graphics.PointF
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weathercomposeapp.data.WeatherModel
import com.example.weathercomposeapp.screens.DialogSearch
import com.example.weathercomposeapp.screens.MainCard
import com.example.weathercomposeapp.screens.TabLayout
import com.example.weathercomposeapp.ui.theme.WeatherComposeAppTheme

const val DEFAULT_CITY = "Yekaterinburg"

class MainActivity : ComponentActivity() {
    private val weatherApi = WeatherApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var isFirstStart = true

        setContent {
            WeatherComposeAppTheme {
                val daysList = remember { mutableStateOf(listOf<WeatherModel>()) }
                val currentDay = remember { mutableStateOf(WeatherModel()) }
                val dialogState = remember { mutableStateOf(false) }
                if (dialogState.value) DialogSearch(
                    dialogState,
                    onSubmit = { getWeather(it, daysList, currentDay) })
                if (isFirstStart) {
                    getWeather(DEFAULT_CITY, daysList, currentDay)
                    isFirstStart = false
                }
                Image(
                    painter = painterResource(id = R.drawable.sky_bg_light),
                    contentDescription = "sky_bg",
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.5f),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    MainCard(currentDay,
                        onClickSearch = { dialogState.value = true },
                        onClickRefresh = {
                            getWeather(DEFAULT_CITY, daysList, currentDay)
                        })
                    TabLayout(daysList.value, currentDay)
                }
            }
        }
    }

    private fun getWeather(
        city: String,
        daysList: MutableState<List<WeatherModel>>,
        currentDay: MutableState<WeatherModel>
    ) {
        weatherApi.getWeather(this, city) {
            daysList.value = it
            currentDay.value = it[0]
        }
    }
}