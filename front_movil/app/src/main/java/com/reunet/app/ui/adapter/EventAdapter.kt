package com.reunet.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.R
import com.reunet.app.databinding.ItemActivityBinding
import com.reunet.app.models.Activity
import com.reunet.app.ui.event.EditEvent

class EventAdapter(
    private val listActivity: List<Activity>,
    ): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private var ACTIVITY: String = "ACTIVITY"

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


    inner class EventViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private val binding = ItemActivityBinding.bind(view)

        var icActivity = ""

        fun bind(activity: Activity, context: Context){
            with(binding){

                if(activity.typeActivity.equals("Task", true)){
                    icActivity = "ic_task"
                }else if(activity.typeActivity.equals("Event", true)){
                    icActivity = "ic_event"
                }else if(activity.typeActivity.equals("Sport", true)){
                    icActivity = "ic_sport"
                }

                val idResA = context.resources.getIdentifier(icActivity, "drawable", context.packageName)

                nameActivity.text = activity.name
                imgActivity.setImageResource(idResA)

                infoActivity.setOnClickListener{
                    val intent = Intent(context, EditEvent::class.java)
                    intent.putExtra(ACTIVITY, activity)
                    context.startActivity(intent)
                }
            }
        }
    }
}

fun ViewGroup.inflate(layoutRes: Int):
        View = LayoutInflater.from(context).inflate(layoutRes, this, false)