package com.reunet.app.ui.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.reunet.app.databinding.FragmentAddEventDialogBinding
import com.reunet.app.models.payload.request.ActivityRequest
import com.reunet.app.services.ActivityService
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEventDialogFragment(
    private val onSaveClickListener: (String) -> Unit,
    sharedPreferencesUtil: SharedPreferenceUtil,
    idGroupP: String
): DialogFragment(){

    private lateinit var binding: FragmentAddEventDialogBinding

    private val activityService by lazy{
        sharedPreferencesUtil.getToken()?.let { ActivityService.build(it) }
    }

    private val idGroup = idGroupP

    //Objects request
    private lateinit var activity: ActivityRequest

    private var typeActivity: String = "Event"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentAddEventDialogBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.activityCloseDate.setOnClickListener {
            showDaterPickerDialog(binding.activityCloseDate)
        }

        binding.activityCloseHour.setOnClickListener {
            showTImePickerDialog(binding.activityCloseHour)
        }

        binding.createActivity.setOnClickListener{
            val selectId = binding.typesRadioGroup.checkedRadioButtonId
            val radio = binding.root.findViewById<RadioButton>(selectId)
            typeActivity = radio.text.toString()

            val name = binding.activityName.text.toString()
            val type = typeActivity
            val closedDate = binding.activityCloseDate.text.toString()
            val closedHour = binding.activityCloseHour.text.toString()

            activity = ActivityRequest(
                name,
                type,
                idGroup,
                "${closedDate}T${closedHour}:00"
            )
            if(validateData(name, closedDate, closedHour)){
                addEvent(activity)
            }
            dismiss()
        }

        binding.cancelCreation.setOnClickListener {
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)
        return dialog
    }


    private fun addEvent(event: ActivityRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val call = activityService?.createActivity(event)
            try{
                if (call != null) {
                    if(call.isSuccessful){
                        val eventResponse = call.body()
                        if(eventResponse != null){
                            Handler(Looper.getMainLooper()).post(Runnable {
                                fun run() {
                                    onSaveClickListener.invoke("Event added successful")
                                }
                                run()
                            })
                        }else{
                            Handler(Looper.getMainLooper()).post(Runnable {
                                fun run() {
                                    onSaveClickListener.invoke("could not add activity")
                                }
                                run()
                            })
                        }
                    }else {
                        throw Exception("credentials are invalid, CODE: ${call.code()}")
                    }
                }
            }catch (e: Exception){
                Log.i("ERROR IN FUNCTION", e.message.toString())
            }
        }
    }

    private fun validateData(name: String,  closeDate:String, closeHour: String): Boolean{
        var validate = false

        if(name.isEmpty()){
            binding.activityName.error = "Activity name must not be empty"
            binding.activityName.requestFocus()
        }

        if(closeDate.equals("YYYY-MM-DD")){
            binding.activityCloseDate.error = "Select a date to close activity"
        }

        if(closeHour.equals("HH-MM")){
            binding.activityCloseHour.error = "Select a hour to close activity"
        }

        if(!name.isEmpty() && !closeDate.equals("YYYY-MM-DD") && !closeHour.equals("HH-MM")){
            validate = true
        }
        return  validate
    }

    private fun showTImePickerDialog(btnHour: Button) {
        val timePicker = TimePickerFragment { onTimeSelected(btnHour, it)}
        timePicker.show(parentFragmentManager, "time")
    }

    private fun onTimeSelected(btnHour: Button, time:String) {
        if(time.length == 5){
            btnHour.text = time
        }else{ //need to add a 0 if it's just a number
            var timeH = ""
            var timeM = ""
            var newTime = ""
           if(time.length == 3) { //if the hour is 3:2 (for example)
               timeH = time.substring(0, 1)
               timeM = time.substring(2, 3)
               newTime += "0$timeH:0$timeM"
               btnHour.text = newTime
           }
            else if(time.length == 4 && !isNumber(time.substring(0, 2))){ //if the hour is 4:30
               timeH = time.substring(0, 1)
               timeM = time.substring(2, 4)
               newTime += "0$timeH:$timeM"
               btnHour.text = newTime
           }
           else if(time.length == 4 && isNumber(time.substring(0, 2))){//if the hour is 10:3
               timeH = time.substring(0, 2)
               timeM = time.substring(3, 4)
               newTime += "$timeH:0$timeM"
               btnHour.text = newTime
           }
        }
    }


    fun showDaterPickerDialog(btnDate: Button){
        val datePicker = DatePickerFragment{day, month, year ->onDateSelected(btnDate, day, month, year)}
        datePicker.show(parentFragmentManager, "datePicker")
    }
    private fun onDateSelected(btnDate:Button, day: Int, month: Int, year: Int){
        val dayP:String = if (day < 10) "0$day" else "$day"
        btnDate.text = "$year-${month+1}-$dayP"
    }

    private fun isNumber(txt:String):Boolean{
        try {
            txt.toInt()
            return true
        }catch (e:Exception){
            return false
        }
    }
}