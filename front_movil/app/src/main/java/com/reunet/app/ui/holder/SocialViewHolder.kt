package com.reunet.app.ui.holder

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ItemGroupChatBinding
import com.reunet.app.models.ChatGroup
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import com.reunet.app.ui.view.chat.ChatActivity

class SocialViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemGroupChatBinding.bind(view)
    private var GROUP: String = "GROUP"
    fun bind(chatGroup: ChatGroup, context: Context){

        with(binding){
            groupName.text = chatGroup.group.name

            dateLastMessage.text = chatGroup.dateLastMessage
            if(chatGroup.lastMessage.count() < 35){
                integrants.text = chatGroup.lastMessage
            }else{
                val resumeMsg = chatGroup.lastMessage.substring(0, 32)
                integrants.text = "$resumeMsg ..."
            }

            groupChat.setOnClickListener{
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra(GROUP, chatGroup.group)
                context.startActivity(intent)
            }
        }
    }
}