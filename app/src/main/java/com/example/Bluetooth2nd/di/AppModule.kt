package com.example.Bluetooth2nd.di

import android.app.Application
import com.example.bluetoothmodule.BluetoothDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMyBt(app:Application): BluetoothDataSource {
        return BluetoothDataSource(app)
    }
}