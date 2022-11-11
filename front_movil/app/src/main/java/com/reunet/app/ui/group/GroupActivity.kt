package com.reunet.app.ui.group

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ActivityGroupsBinding
import com.reunet.app.models.Group
import com.reunet.app.services.GroupService
import com.reunet.app.storage.SharedPreferenceUtil
import com.reunet.app.ui.BaseActivity
import com.reunet.app.ui.event.EventActivity
import com.reunet.app.ui.adapter.GroupAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupActivity : BaseActivity<ActivityGroupsBinding>(ActivityGroupsBinding::inflate) {
    private var ID: String = "ID"

    private lateinit var groups: List<Group>

    private lateinit var recyclerView: RecyclerView

    private val groupService by lazy{
        sharedPreferencesUtil.getToken()?.let { GroupService.build(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(this)
        }


        binding.addGroupBtn.setOnClickListener{
            intent = Intent(this, FormAddGroup::class.java)
            startActivity(intent)
        }

        binding.checkActivities.setOnClickListener{
            intent = Intent(this, EventActivity::class.java)
            startActivity(intent)
        }

        getGroups()
    }

    private fun setRecyclerView(groups: List<Group>){
        recyclerView = binding.groupsList
        recyclerView.layoutManager = LinearLayoutManager(this)
        val groupAdapter = GroupAdapter(groups)
        recyclerView.adapter = groupAdapter
    }

    private fun getGroups(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = groupService?.getGroups()
            try {
                if (call != null) {
                    if (call.isSuccessful){
                        val groupResponse = call.body()
                        if (groupResponse != null){
                            groups = groupResponse.data
                            runOnUiThread{
                                setRecyclerView(groups)
                            }
                        }
                    }else{
                        call.errorBody()?.close()
                    }
                }
            }catch (e: Exception){
                Log.i("Error with coroutines", e.toString())
            }
        }
    }
}