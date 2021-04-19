package com.grument.humanizing

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

abstract class CommonViewModel(dispatcher: CoroutineDispatcher = Dispatchers.IO) : ViewModel() {

    private val backgroundJob: Job = Job()
    protected val backgroundScope: CoroutineScope = CoroutineScope(dispatcher + backgroundJob)


    override fun onCleared() {
        super.onCleared()
        backgroundJob.cancelChildren()
    }
}