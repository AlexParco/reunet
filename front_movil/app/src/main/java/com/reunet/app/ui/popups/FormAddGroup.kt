package com.reunet.app.ui.popups

import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.RadioButton
import android.widget.RadioGroup
import com.reunet.app.databinding.ActivityFormAddGroupBinding
import com.reunet.app.ui.BaseActivity

class FormAddGroup : BaseActivity<ActivityFormAddGroupBinding>
    (ActivityFormAddGroupBinding::inflate),
    //For listen radio buttons
    RadioGroup.OnCheckedChangeListener {
    //Radio variables (Don't much important)
    var radioGroup: RadioGroup? = null
    var btnRadioEvent: RadioButton? = null
    var btnRadioTask: RadioButton? = null
    //activity type  variable
    var typeActivity: String = "Event"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Set view size
        val measureWindow = DisplayMetrics()
        @Suppress("DEPRECATION")
        window.windowManager.defaultDisplay.getMetrics(measureWindow)
        val width:Int = measureWindow.widthPixels
        val height:Int = measureWindow.heightPixels
        window.setLayout(((width * 0.8).toInt()), ((height * 0.50).toInt()))

        radioGroup = binding.radioGroup
        btnRadioEvent = binding.activityEventBtn
        btnRadioTask = binding.activityTaskBtn

        radioGroup?.setOnCheckedChangeListener(this@FormAddGroup)
    }

    override fun onCheckedChanged(p0: RadioGroup?, radio: Int) {
        when (radio){
            btnRadioEvent?.id ->  typeActivity = binding.activityEventBtn.text.toString()
            btnRadioTask?.id -> typeActivity = binding.activityTaskBtn.text.toString()
        }
    }

    fun validateData(gName: String, aName: String, closed:String):Boolean{
        var validate = false
        if(gName.isEmpty()){
            binding.groupName.error = "The group name is required"
            binding.groupName.requestFocus()
        }
        if(aName.isEmpty()){
            binding.groupName.error = "The activity name is required"
            binding.groupName.requestFocus()
        }
        if(closed.isEmpty()){
            binding.activityClose.error = "Closing date is required"
            binding.activityClose.requestFocus()
        }
        if(!gName.isEmpty() && !aName.isEmpty() && !closed.isEmpty()){
            validate = true
        }
        return validate
    }
}