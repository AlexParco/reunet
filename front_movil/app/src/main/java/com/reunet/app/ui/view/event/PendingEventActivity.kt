package com.reunet.app.ui.view.event

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.reunet.app.databinding.ActivityPendingEventBinding
import com.reunet.app.models.Activity
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import com.reunet.app.ui.adapter.EventPendingAdapter
import com.reunet.app.ui.view.BaseActivity
import com.reunet.app.ui.view.MainActivity

class PendingEventActivity :
    BaseActivity<ActivityPendingEventBinding>(ActivityPendingEventBinding::inflate) {
    private val ACTIVITIES = "ACTIVITIES"

    private var adapter: EventPendingAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(this)
        }
        val activities: ArrayList<Activity>? =
            intent.getSerializableExtra(ACTIVITIES) as ArrayList<Activity>?

        if(activities!!.isEmpty()){
            binding.activitiesPending.visibility = View.GONE
            binding.recommendation.visibility = View.GONE
            binding.noPending.visibility = View.VISIBLE
        }

        adapter = EventPendingAdapter(activities)

        binding.activitiesPending.layoutManager = LinearLayoutManager(this)
        binding.activitiesPending.adapter = adapter

        binding.toolbar2.setNavigationOnClickListener{
            finish()
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}