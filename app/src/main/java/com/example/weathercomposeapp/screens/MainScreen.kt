package com.example.weathercomposeapp.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weathercomposeapp.R
import com.example.weathercomposeapp.ui.theme.BlueLight
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.MutableState
import com.example.weathercomposeapp.data.WeatherModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.roundToInt

@Composable
fun MainCard(currentDay: MutableState<WeatherModel>, onClickSearch: () -> Unit, onClickRefresh: () -> Unit) {
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BlueLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = currentDay.value.time,
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    )
                    AsyncImage(
                        model = "https://${currentDay.value.icon}",
                        contentDescription = "weather icon",
                        modifier = Modifier
                            .padding(end = 8.dp, top = 3.dp)
                            .size(35.dp)
                    )
                }
                Text(
                    text = currentDay.value.city,
                    style = TextStyle(fontSize = 24.sp),
                    color = Color.White,
                )
                val tempText =
                    if (currentDay.value.currentTemp.isNotEmpty()) "${currentDay.value.currentTemp}°C"
                    else "${currentDay.value.maxTemp}°C/${currentDay.value.minTemp}°C"
                Text(
                    text = tempText,
                    style = TextStyle(fontSize = 65.sp),
                    color = Color.White,
                )
                Text(
                    text = currentDay.value.condition,
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.White,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        onClickSearch()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "search icon",
                            tint = Color.White
                        )
                    }
                    val maxMinTempText =
                        if (currentDay.value.currentTemp.isNotEmpty()) "${currentDay.value.maxTemp}°C/${currentDay.value.minTemp}°C"
                        else ""
                    Text(
                        text = maxMinTempText,
                        style = TextStyle(fontSize = 16.sp),
                        color = Color.White,
                    )
                    IconButton(onClick = { onClickRefresh() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_refresh),
                            contentDescription = "refresh icon",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout(daysList: List<WeatherModel>, currentDay: MutableState<WeatherModel>) {
    val tabList = listOf("HOURS", "DAYS")
    val pagerState = rememberPagerState()
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp)
            .clip(CardDefaults.shape)
    ) {
        TabRow(
            selectedTabIndex = tabIndex, indicator = { tabPositions ->
                TabRowDefaults.Indicator(Modifier.tabIndicatorOffset(tabPositions[tabIndex]))
            }, containerColor = BlueLight, contentColor = Color.White
        ) {
            tabList.forEachIndexed { index, text ->
                Tab(selected = false, onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }, text = { Text(text = text) })
            }
        }
        HorizontalPager(
            pageCount = tabList.size, state = pagerState, modifier = Modifier.weight(1f)
        ) { index ->
            val list = when (index) {
                0 -> currentDay.value.hours
                1 -> daysList
                else -> daysList
            }
            MainList(list, currentDay)
        }
    }
}

