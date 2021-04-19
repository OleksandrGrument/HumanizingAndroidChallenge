package com.grument.humanizing.view

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grument.humanizing.robot.RobotManager
import com.grument.humanizing.view.main.MainViewModel
import com.grument.jokeslibrary.networking.repository.JokesRepositoryImpl

class VMFactory(application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                JokesRepositoryImpl(),
                RobotManager()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}