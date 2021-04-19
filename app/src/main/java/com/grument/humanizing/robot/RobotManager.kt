package com.grument.humanizing.robot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.grument.humanizing.robot.features.chat.ChatEvent
import com.grument.humanizing.robot.features.chat.ChatManager
import com.grument.humanizing.robot.features.animation.AnimationManager
import com.grument.humanizing.robot.features.animation.AnimationState
import com.grument.humanizing.robot.features.animation.AnimationType

private const val TAG = "RobotManager"

/*
* This is main class to operate all robot features
*/
class RobotManager : RobotLifecycleCallbacks {

    private val chatManager = ChatManager()
    private val animationManager = AnimationManager()

    private val showAnimation = MutableLiveData<Boolean>()

    fun observeShowAnimation(): LiveData<Boolean> = showAnimation
    fun observeChatEvent(): LiveData<ChatEvent> = chatManager.observeChatEvent()

    /**
     * show animation and log Bow animation
     * dispatch show animation to view to lock the button
     */
    suspend fun showAnimation() {

        animationManager.showAnimation(AnimationType.BOW) { state ->

            when (state) {
                AnimationState.START_SHOW_ANIMATION -> {
                    showAnimation.postValue(true)
                    chatManager.runEndBookmark()
                }
                AnimationState.FINISHED_SHOW_ANIMATION -> {
                    Log.d(TAG, "Bow animation ended")
                    showAnimation.postValue(false)
                }
            }
        }
    }

    override fun onRobotFocusGained(qiContext: QiContext) {
        animationManager.prepareAnimations(qiContext)
        chatManager.startChat(qiContext)
    }

    override fun onRobotFocusLost() {
        animationManager.clearAnimation()
        chatManager.disableChat()
    }

    override fun onRobotFocusRefused(reason: String?) {
        animationManager.clearAnimation()
        chatManager.disableChat()
    }
}
