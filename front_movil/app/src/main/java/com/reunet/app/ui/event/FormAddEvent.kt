package com.reunet.app.ui.event

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ActivityFormAddEventBinding
import com.reunet.app.models.Group
import com.reunet.app.models.payload.request.ActivityRequest
import com.reunet.app.services.ActivityService
import com.reunet.app.services.GroupService
import com.reunet.app.storage.SharedPreferenceUtil
import com.reunet.app.ui.BaseActivity
import com.reunet.app.ui.adapter.GroupSelectApadter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormAddEvent : BaseActivity<ActivityFormAddEventBinding>(ActivityFormAddEventBinding::inflate),
    RadioGroup.OnCheckedChangeListener {

    private lateinit var event: ActivityRequest

    private lateinit var groups: List<Group>

    private lateinit var recyclerView: RecyclerView

    private lateinit var itemClickListener: ItemClickListener

    private var groupId:String = ""

    private val activityService by lazy{
        sharedPreferencesUtil.getToken()?.let { ActivityService.build(it) }
    }

    private val groupService by lazy{
        sharedPreferencesUtil.getToken()?.let { GroupService.build(it) }
    }

    //Radio variables (Don't much important)
    var radioGroup: RadioGroup? = null
    var btnRadioEvent: RadioButton? = null
    var btnRadioTask: RadioButton? = null
    var btnRadioSport: RadioButton? = null

    //activity type  variable
    var typeActivity: String = "Event"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idGroup:String = intent.getStringExtra("ID_GROUP").toString()

        if(idGroup.isNullOrEmpty() || idGroup == "null"){
            binding.layoutAddGroup.visibility = View.VISIBLE
        }else{
            groupId = idGroup
            binding.layoutAddGroup.visibility = View.GONE
        }

        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(this)
        }

        radioGroup = binding.radioGroup
        btnRadioEvent = binding.activityEventBtn
        btnRadioSport = binding.activitySportBtn
        btnRadioTask = binding.activityTaskBtn
        radioGroup?.setOnCheckedChangeListener(this@FormAddEvent)

        binding.activityClose.setOnClickListener{
            showDaterPickerDialog(binding.activityClose)
        }

        getGroups()

        //Adding activity

        binding.createActivity.setOnClickListener{
            val name = binding.activityName.text.toString()
            val type = typeActivity
            val closedAt = binding.activityClose.text.toString()

            event = ActivityRequest(
                name,
                type,
                groupId,
                closedAt
            )

            try{
                if(validateData(name, closedAt)){
                    addEvent(event)
                    Toast.makeText(this@FormAddEvent, groupId, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }catch(e: Exception){
                Log.i("Error adding activities", e.message.toString())
            }
        }

    }

    private fun setRecyclerView(groups: List<Group>){
        var eventSelectAdapter = GroupSelectApadter(groups)
        itemClickListener = object : ItemClickListener {
            override fun onClick(s: String) {
                recyclerView.post(object : Runnable {
                    override fun run() {
                        eventSelectAdapter.notifyDataSetChanged()
                    }
                })
                groupId = s
                Toast.makeText(this@FormAddEvent, groupId, Toast.LENGTH_SHORT).show()

            }
        }

        recyclerView = binding.listActivities
        recyclerView.layoutManager = LinearLayoutManager(this)
        eventSelectAdapter = GroupSelectApadter(groups, itemClickListener)
        recyclerView.adapter = eventSelectAdapter
    }


    private fun getGroups(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = groupService?.getGroups()
            try{
                if (call != null) {
                    if(call.isSuccessful){
                        val activityResponse = call.body()
                        if(activityResponse != null){
                            groups = activityResponse.data
                            runOnUiThread{
                                setRecyclerView(groups)
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

    private fun addEvent(event: ActivityRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val call = activityService?.createActivity(event)
            try{
                if (call != null) {
                    if(call.isSuccessful){
                        val eventResponse = call.body()
                        if(eventResponse != null){
                            runOnUiThread{
                                Toast.makeText(this@FormAddEvent, "Event added successful", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else {
                        throw Exception("credentials are invalid, CODE: ${call.code()}")
                    }
                }
            }catch (e: Exception){
                Log.i("Error getting activities", e.toString())
            }
        }
    }

    private fun validateData(name: String,  close:String): Boolean{
        var validate = false
        if(name.isEmpty()){
            binding.activityName.error = "Activity name must not be empty"
            binding.activityName.requestFocus()
        }
        if(close.equals("YYYY-MM-DD")){
            binding.activityClose.error = "Select a date to close activity"
        }

        if(!name.isEmpty() && !close.equals("YYYY-MM-DD")){
            validate = true
        }
        return  validate
    }

    override fun onCheckedChanged(p0: RadioGroup?, radio: Int) {
        when (radio){
            btnRadioEvent?.id ->  typeActivity = binding.activityEventBtn.text.toString()
            btnRadioTask?.id -> typeActivity = binding.activityTaskBtn.text.toString()
            btnRadioSport?.id -> typeActivity = binding.activitySportBtn.text.toString()
        }
    }
}