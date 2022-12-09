package com.reunet.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.R
import com.reunet.app.models.Message
import com.reunet.app.models.MessageStatus
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import com.reunet.app.ui.holder.LeftMessageViewHolder
import com.reunet.app.ui.holder.RightMessageViewHolder

class MessageAdapter(
    var context: Context,
    listMessage: MutableList<MessageStatus>?,
    sender: String,
    receiver: String,
    ): RecyclerView.Adapter<RecyclerView.ViewHolder?>(){
    lateinit var listMessage: MutableList<MessageStatus>

    val ITEM_SENT  = 1
    val ITEM_RECEIVER = 2

    val sender: String
    val receiver: String

    lateinit var  sharedPreferencesUtil: SharedPreferenceUtil


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if ( viewType == ITEM_SENT){
            val view = LayoutInflater.from(context).inflate(R.layout.item_right_message,
                parent, false)
            RightMessageViewHolder(view)
        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.item_left_message,
                parent, false)
            LeftMessageViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(context)
        }

        val messages = listMessage[position]
        return if((sharedPreferencesUtil.getUser()?.id ?: "") == messages.message.userId){
            ITEM_SENT
        }else{
            ITEM_RECEIVER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = listMessage[position]
        if(holder.javaClass == RightMessageViewHolder::class.java){
            val viewHolder = holder as RightMessageViewHolder
            viewHolder.bind(message, context)
        }else{
            val viewHolder = holder as  LeftMessageViewHolder
            viewHolder.bind(message, context)
        }
    }

    override fun getItemCount(): Int = listMessage.size


    init{
        if(listMessage != null){
            this.listMessage = listMessage
        }
        this.sender =  sender
        this.receiver = receiver
    }

}