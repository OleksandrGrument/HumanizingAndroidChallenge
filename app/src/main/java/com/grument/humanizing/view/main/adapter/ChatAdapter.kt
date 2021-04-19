package com.grument.humanizing.view.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grument.humanizing.R
import com.grument.humanizing.robot.features.chat.ChatEvent
import com.grument.humanizing.robot.features.chat.ChatEventType
import kotlinx.android.synthetic.main.item_rv_chat.view.*

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private var eventList = ArrayList<ChatEvent>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_chat, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = eventList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val event = eventList[position]

        val text = when (event.type) {

            ChatEventType.ROBOT_MESSAGE -> "Robot: ${event.message}"
            ChatEventType.HEARD_MESSAGE -> "You: ${event.message}"
            else -> event.message
        }

        holder.messageTv.text = text
    }

    fun handleEvent(event: ChatEvent) {

        if (event.type == ChatEventType.CHAT_FINISHED) {
            eventList.clear()
        }

        eventList.add(event)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTv: TextView = view.messageTv
    }
}






