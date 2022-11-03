package com.reunet.app.ui.events

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ItemActivityBinding
import com.reunet.app.models.Activity
import com.reunet.app.ui.popups.InfoEvent

class EventAdapter(
    private val listActivity: List<Activity>,
): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(
        private val binding: ItemActivityBinding
    ):RecyclerView.ViewHolder(binding.root) {
        val activityName: TextView = binding.nameActivity
        val activityImg: ImageView = binding.imgActivity
        val activityInfo: ImageButton = binding.infoActivity
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val itemActivityBinding = ItemActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(itemActivityBinding)
    }

    override fun onBindViewHolder(holder: EventAdapter.EventViewHolder, position: Int) {
        var icActivity = ""

        val activity: Activity = listActivity[position]
        val context = holder.itemView.context

        if(activity.typeActivity.equals("Task", true)){
            icActivity = "ic_task"
        }else if(activity.typeActivity.equals("Event", true)){
            icActivity = "ic_event"
        }

        val idResA = context.resources.getIdentifier(icActivity, "drawable", context.packageName)

        holder.activityName.text = activity.name
        holder.activityImg.setImageResource(idResA)

        holder.activityInfo.setOnClickListener{
            val intent = Intent(context, InfoEvent::class.java)
            intent.putExtra("ID", activity.id)
            intent.putExtra("NAME", activity.name)
            intent.putExtra("CLOSEDAT", activity.closedAt)
            intent.putExtra("TYPE", activity.typeActivity)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listActivity.size
    }

}