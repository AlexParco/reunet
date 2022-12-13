package com.reunet.app.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.R
import com.reunet.app.models.ChatGroup
import com.reunet.app.ui.holder.SocialViewHolder

class SocialAdapter(
    private val listChatGroup: List<ChatGroup>
): RecyclerView.Adapter<SocialViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SocialViewHolder{
        val itemChatGroup = parent.inflate(R.layout.item_group_chat)
        return SocialViewHolder(itemChatGroup)
    }

    override fun onBindViewHolder(holder: SocialViewHolder, position: Int) {
        val chatGroup: ChatGroup = listChatGroup[position]
        val context = holder.itemView.context
        holder.bind(chatGroup, context)
    }


    override fun getItemCount(): Int {
        return listChatGroup.size
    }
}