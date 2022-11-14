package com.reunet.app.ui.event

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.reunet.app.databinding.ActivityEditEventBinding
import com.reunet.app.models.Activity
import com.reunet.app.models.payload.request.ActivityRequest
import com.reunet.app.services.ActivityService
import com.reunet.app.storage.SharedPreferenceUtil
import com.reunet.app.ui.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditEvent : BaseActivity<ActivityEditEventBinding>(ActivityEditEventBinding::inflate),
    RadioGroup.OnCheckedChangeListener{

    private lateinit var eventUpdate: ActivityRequest

    private var ACTIVITY: String = "ACTIVITY"

    var typeActivity: String = ""

    var nameGroup: String = ""

    var radioGroup: RadioGroup? = null
    var btnRadioEvent: RadioButton? = null
    var btnRadioTask: RadioButton? = null

    private val activityService by lazy{
        sharedPreferencesUtil.getToken()?.let { ActivityService.build(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(this)
        }

        radioGroup = binding.radioGroup
        btnRadioEvent = binding.activityEventBtn
        btnRadioTask = binding.activityTaskBtn
        radioGroup?.setOnCheckedChangeListener(this@EditEvent)

        val event: Activity = intent.getSerializableExtra(ACTIVITY) as Activity

        typeActivity = event.typeActivity
        val dateFormat = event.closedAt.substring(0, 10)

        binding.activityName.setText(event.name)
        binding.activityClose.text = dateFormat

        getGroup(event.groupId.toInt())

        binding.activityClose.setOnClickListener{
            showDaterPickerDialog(binding.activityClose)
        }

        binding.updateActivity.setOnClickListener{
            val name = binding.activityName.text.toString()
            val type = typeActivity
            val closedAt = binding.activityClose.text.toString()

            eventUpdate = ActivityRequest(
                name,
                type,
                event.groupId,
                closedAt
            )

            try{
                updateActivity(event.id.toInt(), eventUpdate)
            }catch (e: Exception){
                Log.i("Error with parameters activity", e.toString())
            }
        }

        binding.deleteActivity.setOnClickListener{
            showConfirmDialog(event.id.toInt())
        }
    }


    private fun updateActivity(id: Int, activity: ActivityRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val call = activityService?.updateActivity(id, activity)
            try{
                if(call != null){
                    if(call.isSuccessful){
                        val eventResponse = call.body()
                        if(eventResponse != null){
                            runOnUiThread{
                                Toast.makeText(this@EditEvent,
                                    "Event updated successfully",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        throw  Exception("ERROR UPDATING")
                    }
                }
            }catch (e: Exception){
                Log.i("Error updating activity in api", e.toString())
            }
        }
    }

    private fun deleteActivity(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val call = activityService?.deleteActivity(id)
            try {
                if(call != null){
                    if(call.isSuccessful){
                        val eventResponse = call.body()
                        if(eventResponse != null){
                            runOnUiThread{
                                Toast.makeText(this@EditEvent,
                                    "Event deleted successfully",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }catch (e: Exception){
                Log.i("Error deleting activity in api", e.toString())
            }
        }
    }

    private fun getGroup(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val call = activityService?.getGroup(id)
            try {
                if(call != null){
                    if(call.isSuccessful){
                        val eventResponse = call.body()
                        if(eventResponse != null){
                            nameGroup = eventResponse.data.name
                            runOnUiThread{
                                binding.groupName.text = nameGroup
                            }
                        }
                    }
                }
            }catch (e: Exception){
                Log.i("Error deleting activity in api", e.toString())
            }
        }
    }

    private fun showConfirmDialog(id: Int){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm")
        builder.setMessage("Are you sure you want to delete the activity?")
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener{dialog, which ->
            deleteActivity(id)
        })

        builder.setNegativeButton("No", DialogInterface.OnClickListener{dialog, which ->
            Toast.makeText(this@EditEvent, "OK", Toast.LENGTH_SHORT).show()
        })

        builder.show()
    }



    override fun onCheckedChanged(p0: RadioGroup?, radio: Int) {
        when (radio){
            btnRadioEvent?.id ->  typeActivity = binding.activityEventBtn.text.toString()
            btnRadioTask?.id -> typeActivity = binding.activityTaskBtn.text.toString()
        }
    }
}