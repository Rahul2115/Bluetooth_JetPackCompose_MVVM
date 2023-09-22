package com.example.bluetooth_jetpackcompose_mvvm.ui.presentation.mainscreen

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.bluetooth_jetpackcompose_mvvm.R


@Composable
fun MainScreen(
    viewModel: ScreenViewModel
) {
    val uiState by viewModel.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 15.dp, bottom = 15.dp)
    ) {
        BtHeader()
        BtSwitch(viewModel = viewModel, uiState = uiState)
        if(uiState.btState)
        {
            LazyColumn{
                item{
                    Text(
                        text = "Paired Devices",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(start = 20.dp, top = 30.dp, bottom = 10.dp)
                    )
                }

                items(uiState.pairedDevicesList.toList()){
                    BtPairedDevices(it,viewModel)
                }
            }
        }
    }
}


@Composable
fun BtHeader(){
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
}

@Composable
fun BtSwitch(viewModel:ScreenViewModel,uiState:ScreenState){
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
            viewModel.btActionChange()
        })
    }
}

@Composable
fun BtPairedDevices(device: BluetoothDevice,viewModel: ScreenViewModel){
    Row(modifier = Modifier
        .padding(
            start = 20.dp, top = 5.dp, bottom = 10.dp, end = 20.dp
        )
        .height(50.dp)
        .clickable {
            Log.d("Tag","Connect to device")
        }) {

        displayIcon(
            device = viewModel.deviceToString(device,1),
            modifier = Modifier
                .size(26.dp)
                .align(Alignment.CenterVertically)
        )

        Text(
            text = viewModel.deviceToString(device,2),
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.bin),
            contentDescription = null,
            modifier = Modifier
                .size(26.dp)
                .align(Alignment.CenterVertically)
                .clickable {
                    try {
                            viewModel.deletePaired(device)
                    } catch (e: Exception) {
                        Log.d("Tag","Delete paired device")
                    }
                }
        )
    }
}

@Composable
fun displayIcon(device: String, modifier: Modifier){
    when (device) {
        "240404" -> Image(
            painter = painterResource(id = R.drawable.headphones),
            contentDescription = null,
            modifier = modifier
        )

        "24010c" -> Image(
            painter = painterResource(id = R.drawable.laptop),
            contentDescription = null,
            modifier = modifier
        )

        "240704" -> Image(
            painter = painterResource(id = R.drawable.watch),
            contentDescription = null,
            modifier = modifier
        )

        "340408" -> Image(painter = painterResource(id = R.drawable.car),
            contentDescription = null,
            modifier = modifier
        )

        "240408" -> Image(painter = painterResource(id = R.drawable.earphone),
            contentDescription = null,
            modifier = modifier
        )

        "240104" -> Image(
            painter = painterResource(id = R.drawable.computer),
            contentDescription = null,
            modifier = modifier
        )

        "5a020c" -> Image(
            painter = painterResource(id = R.drawable.phone),
            contentDescription = null,
            modifier = modifier
        )

        "7a020c" -> Image(
            painter = painterResource(id = R.drawable.apple),
            contentDescription = null,
            modifier = modifier
        )

        else -> Image(painter = painterResource(id = R.drawable.questionmark), contentDescription = null,
            modifier = modifier
        )
    }
}



