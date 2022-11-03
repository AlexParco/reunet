package com.reunet.app.ui.popups

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.reunet.app.databinding.ActivityFormAddEventBinding
import com.reunet.app.models.payload.request.ActivityRequest
import com.reunet.app.services.ActivityService
import com.reunet.app.ui.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Calendar.DAY_OF_MONTH

class FormAddEvent : BaseActivity<ActivityFormAddEventBinding>(ActivityFormAddEventBinding::inflate), RadioGroup.OnCheckedChangeListener {
    private var TOKEN: String = "TOKEN"
    private lateinit var event: ActivityRequest

    private val activityService by lazy{
        ActivityService.build(intent.getStringExtra(TOKEN).toString())
    }

    //Radio variables (Don't much important)
    var radioGroup: RadioGroup? = null
    var btnRadioEvent: RadioButton? = null
    var btnRadioTask: RadioButton? = null
    //activity type  variable
    var typeActivity: String = "Event"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //calculate the screen size
        val measureWindow = DisplayMetrics()
        @Suppress("DEPRECATION")
        window.windowManager.defaultDisplay.getMetrics(measureWindow)
        val width:Int = measureWindow.widthPixels
        val height:Int = measureWindow.heightPixels
        window.setLayout(((width * 0.8).toInt()), ((height * 0.35).toInt()))
        radioGroup = binding.radioGroup
        btnRadioEvent = binding.activityEventBtn
        btnRadioTask = binding.activityTaskBtn
        radioGroup?.setOnCheckedChangeListener(this@FormAddEvent)
        //-----

        //Convert the date
        binding.activityClose.setOnClickListener{
            val now = Calendar.getInstance()
            val datePiker = DatePickerDialog(this, { view, year, month, day ->
                var dayP:String
                if(day < 10){
                    dayP = "0$day"
                }else dayP = "$day"
                binding.activityClose.text = "$year-${month+1}-$dayP"
            },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(DAY_OF_MONTH))
            datePiker.show()
        }

        //Adding activity
        binding.createActivity.setOnClickListener{
            val name = binding.activityName.text.toString()
            val type = typeActivity
            val closedat = binding.activityClose.text.toString().

            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val dateClose = formatter.parse(closedat) as Date

            Toast.makeText(this@FormAddEvent, dateClose.toString(), Toast.LENGTH_SHORT).show()

            event = ActivityRequest(
                dateClose,
                name,
                type
            )

            try{
                if(validateData(name, closedat)){
                    addEvent(event)
                }
            }catch(e: Exception){
                Log.i("Error adding activities", e.message.toString())
            }
        }

    }


    private fun addEvent(event: ActivityRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val call = activityService.createActivity(event)
            try{
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
        }
    }

}