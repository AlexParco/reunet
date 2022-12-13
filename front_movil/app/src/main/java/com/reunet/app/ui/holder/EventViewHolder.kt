package com.reunet.app.ui.holder

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ItemActivityBinding
import com.reunet.app.models.Activity
import com.reunet.app.ui.view.event.EditEvent
import java.text.SimpleDateFormat
import java.util.*

class EventViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private var ACTIVITY: String = "ACTIVITY"

    private val binding = ItemActivityBinding.bind(view)

    private var icActivity = ""

    var icInfoEvent  = ""

    fun bind(activity: Activity, context: Context){
        val actualDate = Calendar.getInstance().time

        val dateSubString = activity.closedAt.substring(0, 10)

        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val dateCompare: Date = sdf.parse(dateSubString) as Date

        val cmp = dateCompare.compareTo(actualDate)

        when{
            cmp > 0 -> {
                icInfoEvent = "ic_info"
            }

            cmp < 0 -> {
                icInfoEvent = "ic_check"
            }

            else -> {
                icInfoEvent  = "ic_for_check"
            }
        }

        if(activity.typeActivity.equals("Task", true))  icActivity = "ic_task"
        else if(activity.typeActivity.equals("Event", true)) icActivity = "ic_event"
        else if(activity.typeActivity.equals("Sport", true)) icActivity = "ic_sport"
        else if(activity.typeActivity.equals("Support", true)) icActivity = "ic_support"

        with(binding){

            val idImgActivity = context.resources
                .getIdentifier(icActivity, "drawable", context.packageName)

            val idIconInfo = context.resources
                .getIdentifier(icInfoEvent, "drawable", context.packageName)

            nameActivity.text = activity.name

            imgActivity.setImageResource(idImgActivity)
            infoActivity.setImageResource(idIconInfo)

            infoActivity.setOnClickListener{
                val intent = Intent(context, EditEvent::class.java)
                intent.putExtra(ACTIVITY, activity)
                context.startActivity(intent)
            }
        }
    }
}