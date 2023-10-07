package com.example.weathercomposeapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weathercomposeapp.data.WeatherModel
import com.example.weathercomposeapp.ui.theme.BlueLight

@Composable
fun ListItem(item: WeatherModel, currentDay: MutableState<WeatherModel>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp)
            .clickable {
                if (item.hours.isNotEmpty()) currentDay.value = item
            },
        colors = CardDefaults.cardColors(containerColor = BlueLight),
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(8.dp, 5.dp)) {
                Text(text = item.time)
                Text(text = item.condition, color = Color.White)
            }
            Text(
                text = if (item.maxTemp.isEmpty()) "${item.currentTemp}°C" else "${item.maxTemp}°C/${item.minTemp}°C",
                color = Color.White,
                style = TextStyle(fontSize = 25.sp)
            )
            AsyncImage(
                model = "https:${item.icon}",
                contentDescription = "weather icon",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(35.dp)
            )
        }
    }
}

@Composable
fun MainList(list: List<WeatherModel>, currentDay: MutableState<WeatherModel>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(list) { _, item ->
            ListItem(item, currentDay)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogSearch(dialogState: MutableState<Boolean>, onSubmit: (String) -> Unit) {
    val dialogText = remember {
        mutableStateOf("")
    }
    AlertDialog(onDismissRequest = { dialogState.value = false }, confirmButton = {
        TextButton(onClick = {
            onSubmit(dialogText.value)
            dialogState.value = false
        }) {
            Text(text = "OK")
        }
    }, dismissButton = {
        TextButton(onClick = { dialogState.value = false }) {
            Text(text = "Cancel")
        }
    }, title = {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Enter the name of the city:", style = TextStyle(fontSize = 16.sp))
            TextField(value = dialogText.value, onValueChange = {
                dialogText.value = it
            })
        }

    })
}