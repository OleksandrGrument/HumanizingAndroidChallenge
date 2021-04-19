package com.grument.humanizing.view.main


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.grument.humanizing.CommonViewModel
import com.grument.humanizing.robot.RobotManager
import com.grument.humanizing.robot.features.chat.ChatEvent
import com.grument.jokeslibrary.data.Result
import com.grument.jokeslibrary.networking.repository.JokesRepository
import kotlinx.coroutines.launch


class MainViewModel(private val jokesRepository:JokesRepository,
                    private val robotManager:RobotManager) : CommonViewModel(), RobotLifecycleCallbacks {


    private val newJoke = MutableLiveData<String>()

    fun observeChatEvent(): LiveData<ChatEvent> = robotManager.observeChatEvent()
    fun observeShowAnimation(): LiveData<Boolean> = robotManager.observeShowAnimation()
    fun observeNewJoke(): LiveData<String> = newJoke

    fun tellAnyJoke() {

        backgroundScope.launch {

            // Handle result from jokes api
            // This is the most primitive way how to do it using as the any result
            // a String representation of the result

            when (val result = jokesRepository.getAnyJoke()) {

                is Result.Success -> newJoke.postValue(result.data.jokeText)
                is Result.Error -> newJoke.postValue("Oops something happened wrong, please try again")
            }
        }
    }

    fun showAnimation() {

        backgroundScope.launch {
            robotManager.showAnimation()
        }
    }

    override fun onRobotFocusGained(qiContext: QiContext) =
        robotManager.onRobotFocusGained(qiContext)

    override fun onRobotFocusLost() =
        robotManager.onRobotFocusLost()

    override fun onRobotFocusRefused(reason: String) =
        robotManager.onRobotFocusRefused(reason)

}