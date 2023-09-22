package com.example.bluetooth_jetpackcompose_mvvm.ui.presentation.mainscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MainScreen(
    viewModel: ScreenViewModel
){
    val uiState by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 15.dp, bottom = 15.dp)
        ) {
            Row(
                modifier = Modifier.padding(
                    start = 10.dp,
                    top = 5.dp,
                    bottom = 5.dp,
                    end = 20.dp
                )
            ) {
                Text(
                    text = "Bluetooth",
                    fontSize = 35.sp, fontWeight = FontWeight.Medium
                )
            }

            //Bluetooth Enable and Disable
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray, shape = RoundedCornerShape(30.dp))
                    .padding(start = 20.dp, top = 10.dp, bottom = 10.dp, end = 20.dp)
            ) {

                Text(
                    text = if (uiState.btState) "On" else "Off",
                    color = Color.Black,
                    fontSize = 25.sp,
                    modifier = Modifier.align(
                        Alignment.CenterVertically
                    ),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))

                Switch(checked = uiState.btState, onCheckedChange = {
                    //navController.navigate("checked_screen")
                    viewModel.switchBtState()
                    Log.d("Bt","${uiState.btState}")
                })
            }
        }
    }
}
