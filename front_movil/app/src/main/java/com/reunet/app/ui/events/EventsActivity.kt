package com.reunet.app.ui.events

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.R
import com.reunet.app.databinding.ActivityEventsBinding
import com.reunet.app.models.Activity
import com.reunet.app.models.Group
import com.reunet.app.services.ActivityService
import com.reunet.app.services.GroupService
import com.reunet.app.ui.BaseActivity
import com.reunet.app.ui.groups.GroupAdapter
import com.reunet.app.ui.groups.GroupsActivity
import com.reunet.app.ui.popups.FormAddEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventsActivity : BaseActivity<ActivityEventsBinding>(ActivityEventsBinding::inflate) {
    private var TOKEN: String = "TOKEN"

    private lateinit var activities: List<Activity>
    private lateinit var recyclerView: RecyclerView

    private val activityService by lazy{
        ActivityService.build(intent.getStringExtra(TOKEN).toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val token = intent.getStringExtra(TOKEN).toString()
        super.onCreate(savedInstanceState)

        binding.addEvent.setOnClickListener{
            Toast.makeText(this, token, Toast.LENGTH_SHORT).show()

            intent = Intent(this, FormAddEvent::class.java)
            intent.putExtra(TOKEN, token)
            startActivity(intent)
        }

        binding.checkGroups.setOnClickListener {
            intent = Intent(this, GroupsActivity::class.java)
            intent.putExtra(TOKEN, token)
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
            val call = activityService.getActivities()
            try{
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
            }catch (e: Exception){
                Log.i("Error getting activities", e.toString())
            }
        }
    }
}