package com.reunet.app.ui.group

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ActivityEditGroupBinding
import com.reunet.app.models.Activity
import com.reunet.app.models.Group
import com.reunet.app.models.payload.request.GroupRequest
import com.reunet.app.services.GroupService
import com.reunet.app.storage.SharedPreferenceUtil
import com.reunet.app.ui.BaseActivity
import com.reunet.app.ui.adapter.EventAdapter
import com.reunet.app.ui.event.FormAddEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditGroup : BaseActivity<ActivityEditGroupBinding>(ActivityEditGroupBinding::inflate) {

    private lateinit var groupUpdate: GroupRequest
    private lateinit var recyclerView: RecyclerView
    private lateinit var activities: List<Activity>

    private var GROUP: String = "GROUP"

    private lateinit var idGroup: String

    private val groupService by lazy{
        sharedPreferencesUtil.getToken()?.let { GroupService.build(it) }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val group: Group = intent.getSerializableExtra(GROUP) as Group

        idGroup = group.id

        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(this)
        }

        binding.groupName.setText(group.name)

        binding.groupDescription.setText(group.description)

        val dateFormat = group.createdAt.substring(0, 10)

        binding.createAt.text = dateFormat

        getActivityByGroupId(group.id.toInt())

        binding.updateGroup.setOnClickListener{
            val name = binding.groupName.text.toString()
            val description = binding.groupDescription.text.toString()
            val user_id = sharedPreferencesUtil.getUser()?.id?.toInt()
            groupUpdate = user_id?.let { it1 ->
                GroupRequest(
                    name,
                    it1,
                    description
                )
            }!!

            try{
                updateGroup(group.id.toInt(), groupUpdate)
            }catch (e: Exception) {
                Log.i("Error with parameters group", e.toString())
            }
        }

        binding.addActivity.setOnClickListener{
            addActivity()
        }
    }

    private fun getActivityByGroupId(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val call = groupService?.getActivityByGroupId(id)
            try {
                if(call != null){
                    if(call.isSuccessful){
                        val eventResponse = call.body()
                        if(eventResponse != null){
                            activities = eventResponse.data
                            runOnUiThread{
                                setRecyclerViewShowActivities(activities)
                            }
                        }
                    }
                }
            }catch (e: Exception){
                Log.i("Error getting activity in api", e.toString())
            }
        }
    }

    private fun addActivity(){
        intent = Intent(this, FormAddEvent::class.java)
        intent.putExtra("ID_GROUP", idGroup)
        startActivity(intent)
    }

    private fun setRecyclerViewShowActivities(activities: List<Activity>){
        recyclerView = binding.activitiesGroup
        recyclerView.layoutManager = LinearLayoutManager(this)
        val activityAdapter = EventAdapter(activities)
        recyclerView.adapter = activityAdapter
    }


    private fun updateGroup(id: Int, group: GroupRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val call = groupService?.updateGroup(id, group)
            try {
                if(call != null){
                    if(call.isSuccessful){
                        val groupResponse = call.body()
                        if(groupResponse != null){
                            runOnUiThread{
                                Toast.makeText(this@EditGroup,
                                    "Group updated successfully",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        throw  Exception("ERROR UPDATING")
                    }
                }
            }catch (e: Exception){
                Log.i("Error updating group in api", e.toString())
            }
        }
    }
}