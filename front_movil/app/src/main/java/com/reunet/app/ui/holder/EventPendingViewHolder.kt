package com.reunet.app.ui.holder

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ItemActivityPendingBinding
import com.reunet.app.models.Activity
import com.reunet.app.models.Group
import com.reunet.app.models.payload.request.ActivityRequest
import com.reunet.app.services.ActivityService
import com.reunet.app.services.GroupService
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class EventPendingViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemActivityPendingBinding.bind(view)
    lateinit var  sharedPreferencesUtil: SharedPreferenceUtil

    private var imgActivityDif = ""

    private val groupService by lazy {
        sharedPreferencesUtil.getToken()?.let {GroupService.build(it)}
        }
    private val activityService by lazy {
        sharedPreferencesUtil.getToken()?.let { ActivityService.build(it)}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(activity: Activity, context: Context){
        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(context)
        }

        // Set images
        if(activity.typeActivity.equals("Task", true))  imgActivityDif = "exam"
        else if(activity.typeActivity.equals("Event", true)) imgActivityDif = "event"
        else if(activity.typeActivity.equals("Sport", true)) imgActivityDif = "balls"
        else if(activity.typeActivity.equals("Support", true)) imgActivityDif = "question"

        val datetimeNow = LocalDateTime.now()

        with(binding){
            val idImgActivity = context.resources
                .getIdentifier(imgActivityDif, "drawable", context.packageName)

            imgActivity.setImageResource(idImgActivity)
            nameActivity.text = activity.name
            toComplete.setOnClickListener{

                val activityRequest = ActivityRequest(
                    activity.name,
                    activity.typeActivity,
                    activity.groupId,
                    datetimeNow.toString())

                updateDateActivity(context, activity.id.toInt(), activityRequest)

                toComplete.visibility = View.GONE
                animationCheck.visibility = View.VISIBLE
                animationCheck.playAnimation()
            }
        }
        defNameGroup(activity.groupId.toInt())
    }

    fun defNameGroup(id: Int){
        CoroutineScope(Dispatchers.IO).launch{
            val call = groupService?.getGroupById(id)
            val callResponse: Group = call!!.body()!!.data
            Handler(Looper.getMainLooper()).post(Runnable { //It is queued to the main thread
            fun run() {
                binding.groupActivity.text = "Group: ${callResponse.name}"
            }
                run()
            })
        }
    }

    fun updateDateActivity(context: Context, idActivity:Int, activity: ActivityRequest){
        CoroutineScope(Dispatchers.IO).launch{
            val call = activityService!!.updateActivity(idActivity, activity)
            if(call.isSuccessful){
                Handler(Looper.getMainLooper()).post(Runnable { //It is queued to the main thread
                fun run() {
                    Toast.makeText(context, "The activity finished successfully", Toast.LENGTH_SHORT).show()
                }
                    run()
                })
            }
        }
    }
}