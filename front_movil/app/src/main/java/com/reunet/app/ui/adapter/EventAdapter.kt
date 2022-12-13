package com.reunet.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.R
import com.reunet.app.models.Activity
import com.reunet.app.ui.holder.EventViewHolder

class EventAdapter(
    private val listActivity: List<Activity>,
    ): RecyclerView.Adapter<EventViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {

        val itemActivityBinding = parent.inflate(R.layout.item_activity)
        return EventViewHolder(itemActivityBinding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val activity: Activity = listActivity[position]
        val context = holder.itemView.context
        holder.bind(activity, context)
    }

    override fun getItemCount(): Int {
        return listActivity.size
    }

}

fun ViewGroup.inflate(layoutRes: Int):
        View = LayoutInflater.from(context).inflate(layoutRes, this, false)