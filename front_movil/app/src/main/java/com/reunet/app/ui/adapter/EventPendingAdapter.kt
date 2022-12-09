package com.reunet.app.ui.adapter

import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.R
import com.reunet.app.models.Activity
import com.reunet.app.ui.holder.EventPendingViewHolder

class EventPendingAdapter(
    listActivity: MutableList<Activity>?
): RecyclerView.Adapter<EventPendingViewHolder>(){

    lateinit var listActivity: MutableList<Activity>

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventPendingViewHolder {
        val itemActivityPending = parent.inflate(R.layout.item_activity_pending)
        return EventPendingViewHolder(itemActivityPending)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EventPendingViewHolder, position: Int) {
        val activity = listActivity[position]
        val context = holder.itemView.context
        holder.bind(activity, context)
    }

    override fun getItemCount(): Int {
        return listActivity.size
    }

    init{
        if(listActivity != null){
            this.listActivity = listActivity
        }
    }
}