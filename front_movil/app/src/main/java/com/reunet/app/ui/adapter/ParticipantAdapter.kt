package com.reunet.app.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.R
import com.reunet.app.models.User
import com.reunet.app.ui.holder.ParticipantViewHolder

class ParticipantAdapter(
    private val listParticipant: List<User>
): RecyclerView.Adapter<ParticipantViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val itemParticipantBinding = parent.inflate(R.layout.item_participant)
        return ParticipantViewHolder(itemParticipantBinding)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        val participant: User = listParticipant[position]
        val context = holder.itemView.context
        holder.bind(participant, context)
    }

    override fun getItemCount(): Int {
        return listParticipant.size
    }
}