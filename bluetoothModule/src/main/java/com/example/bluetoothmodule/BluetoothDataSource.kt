@file:Suppress("DEPRECATION")

package com.example.bluetoothmodule

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

class BluetoothDataSource(private val context:Context) {

    private var mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    //Handles Bluetooth Turn On and off
    fun changeBtAction(){
        if (ActivityCompat.checkSelfPermission(
                /* context = */ context,
                /* permission = */ Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Bt","In btOn")
            if (mBluetoothAdapter.isEnabled) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU )
                {
                    Toast.makeText(context,"Turn off bluetooth from you device settings.", Toast.LENGTH_LONG).show()
                }else{
                    mBluetoothAdapter.disable()
                }
            } else {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(enableBtIntent)
                }else{
                    mBluetoothAdapter.enable()
                }
            }
        }
    }

    //Return the Bluetooth State
    fun isBtOn() : Boolean{
        return mBluetoothAdapter.isEnabled
    }

    fun isDiscovering() : Boolean{
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return mBluetoothAdapter.isDiscovering
    }

    fun discoverDevices() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (mBluetoothAdapter.isDiscovering) {
                mBluetoothAdapter.cancelDiscovery()
            }
            mBluetoothAdapter.startDiscovery()
            Log.d("Discover", mBluetoothAdapter.isDiscovering.toString())
        }
    }

    fun cancelDiscovery(){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mBluetoothAdapter.cancelDiscovery()
    }

    fun getPaired() : Set<BluetoothDevice> {
        var pairedDevices:  Set<BluetoothDevice> = mutableSetOf()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            pairedDevices = mBluetoothAdapter.bondedDevices!!
        }
        return pairedDevices
    }

    fun getDeviceName() : String {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return ""
        }
        return mBluetoothAdapter.name
    }

    fun setDeviceName(name:String){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mBluetoothAdapter.name = name
    }

    fun makeDiscover(){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (!mBluetoothAdapter.isDiscovering) {
                val discoverableIntent: Intent =
                    Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                        putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                context.startActivity(discoverableIntent)
            }
        }
    }

}