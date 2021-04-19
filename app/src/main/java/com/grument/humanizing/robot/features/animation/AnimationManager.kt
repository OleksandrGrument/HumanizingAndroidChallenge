package com.grument.humanizing.robot.features.animation

import android.util.Log
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.actuation.Animate
import com.aldebaran.qi.sdk.builder.AnimateBuilder
import com.aldebaran.qi.sdk.builder.AnimationBuilder


private const val TAG = "Animation Manager"

class AnimationManager {

    private var animatePrepared = HashMap<AnimationType, Animate>()

    /**
     * Prepare animate objects when pepper gain focus
     */
    fun prepareAnimations(qiContext: QiContext) {

        Log.d(TAG, "Stated preparing animations")

        AnimationType.values().forEach { type ->

            val animation = AnimationBuilder.with(qiContext)
                .withResources(type.resId)
                .build()

            val animate = AnimateBuilder.with(qiContext)
                .withAnimation(animation)
                .build()

            animatePrepared[type] = animate
        }

        Log.d(TAG, "Finished preparing animations")
    }

    // Show animation by type
    suspend fun showAnimation(type: AnimationType,
                              stateCallback: (AnimationState) -> Unit) {

        // get animation from prepared animations
        val animate = animatePrepared[type]

        animate?.let {

            // set callback on start showing
            animate.addOnStartedListener {
                Log.d(TAG, "Started showing $type animation")
                stateCallback.invoke(AnimationState.START_SHOW_ANIMATION)
            }

            // Run the animate action async
            val animateFuture = animate.async().run()

            // set callback on animation finished
            animateFuture.thenConsume {
                Log.d(TAG, "Finished showing $type animation")
                stateCallback.invoke(AnimationState.FINISHED_SHOW_ANIMATION)
            }
        }
    }

    fun clearAnimation() = animatePrepared.clear()
}

enum class AnimationState {
    START_SHOW_ANIMATION,
    FINISHED_SHOW_ANIMATION
}