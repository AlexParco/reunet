package com.reunet.app.ui.view.group

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.reunet.app.databinding.ActivityInfoGroupBinding
import com.reunet.app.models.Activity
import com.reunet.app.models.Group
import com.reunet.app.models.Participant
import com.reunet.app.models.User
import com.reunet.app.models.payload.request.GroupRequest
import com.reunet.app.services.ActivityService
import com.reunet.app.services.GroupService
import com.reunet.app.services.ParticipantService
import com.reunet.app.services.UserService
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import com.reunet.app.ui.view.BaseActivity
import com.reunet.app.ui.adapter.EventAdapter
import com.reunet.app.ui.adapter.ParticipantAdapter
import com.reunet.app.ui.fragment.AddEventDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InfoGroupActivity : BaseActivity<ActivityInfoGroupBinding>(ActivityInfoGroupBinding::inflate){

    //Group request class
    private lateinit var groupUpdate: GroupRequest

    //Adapters
    private lateinit var eventAdapter: EventAdapter
    private lateinit var participantAdapter: ParticipantAdapter

    //Group activities list for adapter
    private val activities = mutableListOf<Activity>()
    private val users = mutableListOf<User>()
    private val participants = mutableListOf<Participant>()

    private val auxListUser = ArrayList<User>()

    //Access variables
    private val GROUP: String = "GROUP"

    //Error variables
    private val ERROR_IN_FUNCTION: String = "ERROR IN FUNCTION"
    private val ERROR_IN_API: String = "ERROR IN API"

    //Global id (principal id)
    private lateinit var idGroup: String

    //get services
    private val groupService by lazy{
        sharedPreferencesUtil.getToken()?.let { GroupService.build(it) }
    }

    private val activityService by lazy {
        sharedPreferencesUtil.getToken()?.let { ActivityService.build(it)}
    }

    private val userService by lazy {
        sharedPreferencesUtil.getToken()?.let { UserService.build(it)}
    }

    private val participantService by lazy {
        sharedPreferencesUtil.getToken()?.let { ParticipantService.build(it)}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //get group from intent
        val group: Group = intent.getSerializableExtra(GROUP) as Group

        //Set id of obtained group
        idGroup = group.id

        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(this)
        }

        val formatDate = group.createdAt.substring(0, 10)

        binding.createAt.text = formatDate

        getParticipantsByGroup(group.id)

        initRecyclerViewParticipants()

        //If is the group creator
        if(group.userId == sharedPreferencesUtil.getUser()?.id){

            binding.groupNameEdit.setText(group.name)

            binding.groupDescriptionEdit.setText(group.description)

            getActivityByGroupId(group.id)

            initRecyclerViewActivities()


            binding.updateGroup.setOnClickListener{

                val name = binding.groupNameEdit.text.toString()
                val description = binding.groupDescriptionEdit.text.toString()
                val user_id = sharedPreferencesUtil.getUser()?.id?.toInt()

                //Building object to update
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

            binding.deleteGroup.setOnClickListener{
                showConfirmDialog(group.id.toInt())
            }
        //If is a participant
        }else{
            hideAndShowForParticipant()
            binding.nameGroupToolbar.text = group.name
            binding.groupDescription.text = group.description
            getCreatorGroup(group.userId.toInt())
            binding.leaveGroup.setOnClickListener{
                leaveGroup(group.id.toInt())
            }
        }
    }

    //functions for the initialization of the recycler view
    private fun initRecyclerViewActivities(){
        eventAdapter = EventAdapter(activities)
        binding.activitiesGroup.layoutManager = LinearLayoutManager(this@InfoGroupActivity)
        binding.activitiesGroup.adapter = eventAdapter
    }

    private fun initRecyclerViewParticipants(){
        participantAdapter = ParticipantAdapter(users)
        binding.participantsGroup.layoutManager = LinearLayoutManager(this@InfoGroupActivity)
        binding.participantsGroup.adapter = participantAdapter
    }


    //ACTION FOR PARTICIPANT AND CREATOR
    private fun getParticipantsByGroup(idGroup: String){
        CoroutineScope(Dispatchers.IO).launch {
            val callParticipant = participantService?.getParticipantsByGroup(idGroup)
            try {
                if(callParticipant!!.isSuccessful){
                    val participantResponse = callParticipant.body()
                    val participantsGroup: List<Participant> = participantResponse?.data ?: emptyList()
                    if (participantsGroup.size > 0){
                        participants.clear()
                        participants.addAll(participantsGroup)
                        for(participant in participants){
                            val callUser = userService?.getUserById(participant.userId)
                            val userResponse = callUser?.body()?.data
                            userResponse?.let { auxListUser.add(it) }
                        }
                        runOnUiThread {
                            users.clear()
                            users.addAll(auxListUser.toList())
                            participantAdapter.notifyDataSetChanged()
                            binding.numberParticipants.text = "${users.size} participants"
                        }
                    }else{
                        runOnUiThread {
                            users.clear()
                            users.addAll(auxListUser.toList())
                            participantAdapter.notifyDataSetChanged()
                            binding.numberParticipants.text = "No participants"
                        }
                    }
                }else{
                    Log.i(ERROR_IN_API, "Error getting participants")
                }
            }catch(e: Exception){
                Log.i(ERROR_IN_FUNCTION,  e.toString())
            }
        }
    }


    //ACTION FOR PARTICIPANT
    private fun getCreatorGroup(idUser: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val call = userService?.getUserById(idUser)
            try{
                if(call!!.isSuccessful){
                    val userResponse = call.body()
                    val user: User? = userResponse?.data
                    runOnUiThread {
                        if (user != null) {
                            binding.createBy.text = "Created by ${user.firstname} ${user.lastname}"
                        }
                    }
                }else{
                    Log.i(ERROR_IN_API, "Error getting creator")
                }
            }catch(e: Exception){
                Log.i(ERROR_IN_FUNCTION, e.toString())
            }
        }
    }

    private fun leaveGroup(idGroup: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val callGet = participantService?.getParticipantsByGroup(idGroup.toString())
            try{
                if(callGet!!.isSuccessful){
                    val participantResponse = callGet.body()
                    val participantsGroup: List<Participant> = participantResponse?.data ?: emptyList()
                    for(i in participantsGroup){
                        if(i.userId.toString() == (sharedPreferencesUtil.getUser()?.id ?: "")){
                            val callDelete = participantService?.deleteParticipant(i.participantId)
                            if(callDelete!!.isSuccessful){
                                runOnUiThread {
                                    Toast.makeText(
                                        this@InfoGroupActivity,
                                        "Exited the group successfully :)",
                                        Toast.LENGTH_SHORT).show()
                                }
                                delay(200)
                                runOnUiThread {
                                    val intent = Intent(this@InfoGroupActivity, GroupActivity::class.java)
                                    startActivity(intent)
                                }
                                finish()
                            }else{
                                runOnUiThread {
                                    Toast.makeText(
                                        this@InfoGroupActivity,
                                        "Could not leave the group :(",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }catch(e: Exception){
                Log.i(ERROR_IN_FUNCTION, e.toString())
            }
        }
    }


    //ACTIONS FOR GROUP CREATOR

    private fun getActivityByGroupId(idGroup: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = groupService?.getActivityByGroupId(idGroup)
            try {
                runOnUiThread {
                    if (call!!.isSuccessful) {
                        val eventResponse = call.body()
                        if (eventResponse != null) {
                            val activitiesGroup:List<Activity> = eventResponse.data
                            binding.numberActivities.text = activitiesGroup.size.toString() + " activities"
                            activities.clear()
                            activities.addAll(activitiesGroup)
                            eventAdapter.notifyDataSetChanged()
                        }
                    } else {
                        Toast.makeText(
                            this@InfoGroupActivity,
                            "An error has occurred",
                            Toast.LENGTH_SHORT).show()
                        Log.i(ERROR_IN_API, "Getting activities")
                    }
                }
            }catch (e: Exception){
                Log.i(ERROR_IN_FUNCTION, e.toString())
            }
        }
    }

    private fun addActivity(){
        AddEventDialogFragment(
            onSaveClickListener = { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            },
            sharedPreferencesUtil,
            idGroup
        ).show(supportFragmentManager, "DIALOG")
    }


    private fun updateGroup(id: Int, group: GroupRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val call = groupService?.updateGroup(id, group)
            try {
                if(call!!.isSuccessful){
                    val groupResponse = call.body()
                    if(groupResponse != null){
                        runOnUiThread{
                            Toast.makeText(this@InfoGroupActivity,
                                "Group updated successfully",
                                Toast.LENGTH_SHORT).show()
                        }
                        delay(200)
                        runOnUiThread {
                            val intent = Intent(this@InfoGroupActivity, GroupActivity::class.java)
                            startActivity(intent)
                        }
                        finish()
                    }
                }else{
                    Log.i(ERROR_IN_API, "Updating group failed")
                }
            }catch (e: Exception){
                Log.i(ERROR_IN_FUNCTION, e.toString())
            }
        }
    }

    //Delete group with its activities and participants
    private fun deleteGroup(idGroup: String){
        CoroutineScope(Dispatchers.IO).launch {
            val callActivity = groupService?.getActivityByGroupId(idGroup)
            val callParticipant = participantService?.getParticipantsByGroup(idGroup)
            try {
                if (callActivity?.isSuccessful == true && callParticipant?.isSuccessful == true) {
                    val eventResponse = callActivity.body()
                    val participantResponse = callParticipant.body()
                    val activitiesGroup: List<Activity> = eventResponse?.data ?: emptyList()
                    val participantGroup: List<Participant> = participantResponse?.data ?: emptyList()

                    for (i in activitiesGroup) {
                        activityService?.deleteActivity(i.id.toInt())
                    }

                    for(i in participantGroup){
                        participantService?.deleteParticipant(i.participantId)
                    }

                    groupService?.deleteGroup(idGroup.toInt())
                    runOnUiThread {
                        Toast.makeText(
                            this@InfoGroupActivity,
                            "Group deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    delay(200)
                    runOnUiThread {
                        val intent = Intent(this@InfoGroupActivity, GroupActivity::class.java)
                        startActivity(intent)
                    }
                    finish()
                } else {
                    Log.i(ERROR_IN_API, "deleting group")
                    callActivity?.errorBody()?.close()
                    callParticipant?.errorBody()?.close()
                }
            }catch (e: Exception){
                Log.i(ERROR_IN_FUNCTION, e.toString())
            }
        }
    }


    private fun showConfirmDialog(id: Int){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm")
        builder.setMessage("Are you sure you want to delete this group? The activities will also be deleted")

        builder.setPositiveButton("Yes", DialogInterface.OnClickListener{ dialog, which ->
            deleteGroup(id.toString())
        })

        builder.setNegativeButton("No", DialogInterface.OnClickListener{ dialog, which ->

        })
        builder.show()
    }

    private fun hideAndShowForParticipant(){
        binding.containerActions.visibility = View.GONE
        binding.addActivity.visibility = View.GONE
        binding.containerActivities.visibility = View.GONE

        binding.groupDescriptionEdit.visibility = View.GONE
        binding.groupNameEdit.visibility = View.GONE

        binding.groupDescription.visibility = View.VISIBLE
        binding.containerActionsParticipant.visibility = View.VISIBLE
        binding.createBy.visibility = View.VISIBLE
    }
}