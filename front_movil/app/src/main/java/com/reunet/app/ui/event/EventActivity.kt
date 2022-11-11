package com.reunet.app.ui.event

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ActivityEventsBinding
import com.reunet.app.models.Activity
import com.reunet.app.services.ActivityService
import com.reunet.app.storage.SharedPreferenceUtil
import com.reunet.app.ui.BaseActivity
import com.reunet.app.ui.adapter.EventAdapter
import com.reunet.app.ui.group.GroupActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventActivity : BaseActivity<ActivityEventsBinding>(ActivityEventsBinding::inflate) {

    private lateinit var activities: List<Activity>
    private lateinit var recyclerView: RecyclerView

    private val activityService by lazy{
        sharedPreferencesUtil.getToken()?.let { ActivityService.build(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(this)
        }


        binding.addEvent.setOnClickListener{
            intent = Intent(this, FormAddEvent::class.java)
            startActivity(intent)
        }

        binding.checkGroups.setOnClickListener {
            intent = Intent(this, GroupActivity::class.java)
            startActivity(intent)
        }

        getActivities()
    }

    private fun setRecyclerView(activities: List<Activity>){
        recyclerView = binding.activityList
        recyclerView.layoutManager = LinearLayoutManager(this)
        val activityAdapter = EventAdapter(activities)
        recyclerView.adapter = activityAdapter
    }

    private fun getActivities(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = activityService?.getActivities()
            try{
                if (call != null) {
                    if(call.isSuccessful){
                        val activityResponse = call.body()
                        if(activityResponse != null){
                            activities = activityResponse.data
                            runOnUiThread{
                                setRecyclerView(activities)
                            }
                        }
                    }else{
                        Log.i("Error getting activities", "${call.code()}")
                    }
                }
            }catch (e: Exception){
                Log.i("Error getting activities", e.toString())
            }
        }
    }
}