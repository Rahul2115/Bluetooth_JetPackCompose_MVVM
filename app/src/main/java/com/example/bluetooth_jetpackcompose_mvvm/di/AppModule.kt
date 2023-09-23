package com.example.bluetooth_jetpackcompose_mvvm.di

import android.app.Application
import com.example.bluetoothmodule.BtActions
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
    fun provideMyBt(app:Application): BtActions {
        return BtActions(app)
    }
}