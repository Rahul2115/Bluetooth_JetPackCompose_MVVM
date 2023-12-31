package com.example.Bluetooth2nd.ui.presentation.mainscreen

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.Bluetooth2nd.R
import com.example.Bluetooth2nd.Screen


@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: ScreenViewModel,navController: NavController,uiState: ScreenState) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Scaffold(
        bottomBar = {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Divider(color = Color.LightGray, thickness = 2.dp)

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    //modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(
                        Modifier
                            .weight(4f)
                            .background(Color.Red))
                    if (uiState.btState) {
                        Button(
                            onClick = {
                                viewModel.makeDeviceDiscover()
                            },
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                        ) {
                            Text(text = "Make Discoverable")
                        }
                    }

                    Spacer(
                        Modifier
                            .weight(1f)
                            .background(Color.Green))

                    IconButton(onClick = {},interactionSource = interactionSource) {
                        if(isPressed){
                            Log.d("Voice","Listening")
                            Icon(
                                painter = painterResource(id = R.drawable.microphone),
                                contentDescription = null,
                            )

                            viewModel.startListen()
                        }else{
                            Icon(
                                painter = painterResource(id = R.drawable.mic),
                                contentDescription = null,
                            )
                            viewModel.speechRecognizer.stopListening()
                        }

                    }

                    Spacer(
                        Modifier
                            .weight(1f)
                            .background(Color.Green))

                }

            }
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(top = 20.dp)
        ) {
            BtHeader(viewModel, uiState)

            BtSwitch(viewModel = viewModel, uiState = uiState)

            DeviceName(uiState, navController,viewModel)

            if (uiState.btState) {
                LazyColumn {
                    item {
                        Text(
                            text = "Paired Devices",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(start = 20.dp, top = 30.dp, bottom = 10.dp)
                        )
                    }

                    if (uiState.pairedDevicesList.isNotEmpty()) {
                        items(uiState.pairedDevicesList.toList()) {
                            BtPairedDevices(it, viewModel)
                        }
                    } else {
                        item {
                            Text(
                                text = "No paired devices found",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier
                                    .padding(
                                        start = 20.dp,
                                    )
                                    .height(50.dp)
                            )
                        }
                    }

                    item {
                        Text(
                            text = "Available Devices",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(start = 20.dp, top = 10.dp, bottom = 15.dp)
                        )
                    }

                    if (uiState.availableDeviceList.isNotEmpty()) {
                        items(uiState.availableDeviceList.toList()) {
                            AvailableDevices(device = it, viewModel = viewModel)
                        }
                    } else {
                        item {
                            Text(
                                text = "No devices found",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier
                                    .padding(
                                        start = 20.dp,
                                    )
                                    .height(50.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BtHeader(viewModel:ScreenViewModel,uiState:ScreenState){
    Row(
        modifier = Modifier.padding(
            start = 10.dp,
            top = 5.dp,
            bottom = 15.dp,
            end = 20.dp
        )
    ) {
        Text(
            text = "Bluetooth",
            fontSize = 35.sp, fontWeight = FontWeight.Medium
        )

        if(uiState.btState){
            Spacer(modifier = Modifier.weight(1f))

            if(!uiState.discoverState) {
                Button(onClick = {
                    viewModel.getAvailableDevices()
                }) {
                    Text(text = "Scan")
                }

            }else{
                Button(onClick = {
                    viewModel.cancelDiscovery()
                }) {
                    Text(text = "Stop")
                }
            }
        }
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
fun DeviceName(uiState: ScreenState,navController: NavController,viewModel: ScreenViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp, start = 10.dp)
            .clickable {
                if (uiState.btState) {
                    navController.navigate(Screen.NameScreen.route)
                } else {
                    viewModel.toast("Turn on Bluetooth to continue.")
                }
            }
    ) {

        Text(
            text = "Device name",
            fontSize = 18.sp,
            modifier = Modifier
                .padding(
                    start = 10.dp,
                )
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = uiState.deviceName ,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(
                    start = 10.dp, end = 10.dp
                )
                .align(Alignment.CenterVertically)
        )

        Image(
            painter = painterResource(id = R.drawable.rightarrow),
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .align(Alignment.CenterVertically)
                .padding(end = 20.dp)
        )
    }
}


@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun BtPairedDevices(device: BluetoothDevice,viewModel: ScreenViewModel){
    Row(modifier = Modifier
        .padding(
            start = 20.dp, top = 5.dp, bottom = 10.dp, end = 20.dp
        )
        .height(50.dp)
        .clickable {
            Log.d("Tag", "Connect to device")
        }) {

        DisplayIcon(
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
                        Log.d("Tag", "Delete paired device")
                    }
                }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun AvailableDevices(device: BluetoothDevice,viewModel: ScreenViewModel){
    Row(
        modifier = Modifier
            .padding(
                start = 20.dp, top = 5.dp, bottom = 10.dp, end = 20.dp
            )
            .height(50.dp)
            .clickable {
                viewModel.connect(device)
            }
            .fillMaxWidth()
    ) {

        DisplayIcon(
            device = viewModel.deviceToString(device,1),
            modifier = Modifier
                .size(24.dp)
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
    }

}


@Composable
fun DisplayIcon(device: String, modifier: Modifier){
    when (device) {
        stringResource(R.string.headphones)-> Image(
            painter = painterResource(id = R.drawable.headphones),
            contentDescription = null,
            modifier = modifier
        )

        stringResource(R.string.laptop) -> Image(
            painter = painterResource(id = R.drawable.laptop),
            contentDescription = null,
            modifier = modifier
        )

        stringResource(R.string.watch) -> Image(
            painter = painterResource(id = R.drawable.watch),
            contentDescription = null,
            modifier = modifier
        )

        stringResource(R.string.car) -> Image(painter = painterResource(id = R.drawable.car),
            contentDescription = null,
            modifier = modifier
        )

        stringResource(R.string.earphone) -> Image(painter = painterResource(id = R.drawable.earphone),
            contentDescription = null,
            modifier = modifier
        )

        stringResource(R.string.computer) -> Image(
            painter = painterResource(id = R.drawable.computer),
            contentDescription = null,
            modifier = modifier
        )

        stringResource(R.string.phone) -> Image(
            painter = painterResource(id = R.drawable.phone),
            contentDescription = null,
            modifier = modifier
        )

        stringResource(R.string.apple) -> Image(
            painter = painterResource(id = R.drawable.apple),
            contentDescription = null,
            modifier = modifier
        )

        else -> Image(painter = painterResource(id = R.drawable.questionmark), contentDescription = null,
            modifier = modifier
        )
    }
}



