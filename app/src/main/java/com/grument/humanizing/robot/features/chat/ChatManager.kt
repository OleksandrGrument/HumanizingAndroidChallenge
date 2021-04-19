package com.grument.humanizing.robot.features.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.conversation.*
import com.aldebaran.qi.sdk.builder.ChatBuilder
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder
import com.aldebaran.qi.sdk.builder.TopicBuilder
import com.grument.humanizing.R

private const val TAG = "ChatManager"

class ChatManager {

    private var chat: Chat? = null

    private lateinit var startBookmark: Bookmark
    private lateinit var endBookmark: Bookmark
    private lateinit var qiChatbot: QiChatbot

    private val chatEvent = MutableLiveData<ChatEvent>()

    fun observeChatEvent(): LiveData<ChatEvent> = chatEvent

    /**
     * This function initiliaze chat and run start bookmark
     */
    fun startChat(qiContext: QiContext) {

        val topic = createTopic(qiContext)

        initBookmarks(topic)

        qiChatbot = QiChatbotBuilder.with(qiContext)
            .withTopic(topic)
            .build()

        // Initialize chat
        val chat = ChatBuilder.with(qiContext)
            .withChatbot(qiChatbot)
            .build()
            .also { this.chat = it }


        // Add listeners for all chat actions
        chat.addOnStartedListener {
            Log.d(TAG, "chat started")
            chatEvent.postValue(ChatEvent(ChatEventType.CHAT_STARTED, "Chat started"))
        }
        chat.addOnHeardListener {
            if (it.text.isNotBlank()) {
                Log.d(TAG, "heard message ${it.text}")
                chatEvent.postValue(ChatEvent(ChatEventType.HEARD_MESSAGE, it.text))
            }
        }
        chat.addOnSayingChangedListener {
            if (it.text.isNotBlank()) {
                Log.d(TAG, "robot message ${it.text}")
                chatEvent.postValue(ChatEvent(ChatEventType.ROBOT_MESSAGE, it.text))
            }
        }

        val chatFuture = chat.async().run()

        chatFuture.thenConsume {

            val message = if (it.hasError()) {
                Log.d(TAG, "finished successfully")
                "Chat finished with error"
            } else {
                Log.d(TAG, "finished with error ${it.errorMessage}")
                "Chat finished"
            }

            chatEvent.postValue(ChatEvent(ChatEventType.CHAT_FINISHED, message))
        }

        runStartBookmark()
    }

    private fun createTopic(qiContext: QiContext): Topic {

        return TopicBuilder.with(qiContext)
            .withResource(R.raw.welcome)
            .build()
    }

    private fun initBookmarks(topic: Topic) {

        topic.apply {
            bookmarks["welcoming"]?.let { startBookmark = it }
            bookmarks["goodbye"]?.let { endBookmark = it }
        }
    }

    private fun runStartBookmark() {
        qiChatbot.async().goToBookmark(
            startBookmark,
            AutonomousReactionImportance.HIGH,
            AutonomousReactionValidity.IMMEDIATE
        )
    }

    fun runEndBookmark() {
        qiChatbot.async().goToBookmark(
            endBookmark,
            AutonomousReactionImportance.HIGH,
            AutonomousReactionValidity.IMMEDIATE
        )
    }

    fun disableChat() {
        chat?.removeAllOnHeardListeners()
        chat?.removeAllOnStartedListeners()
        chat?.removeAllOnSayingChangedListeners()
        chat = null
    }
}

data class ChatEvent(
    val type: ChatEventType,
    val message: String = ""
)

enum class ChatEventType {
    CHAT_STARTED,
    HEARD_MESSAGE,
    ROBOT_MESSAGE,
    CHAT_FINISHED
}