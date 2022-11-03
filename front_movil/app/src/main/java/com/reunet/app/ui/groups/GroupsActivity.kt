package com.reunet.app.ui.groups

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ActivityGroupsBinding
import com.reunet.app.models.Group
import com.reunet.app.services.GroupService
import com.reunet.app.ui.BaseActivity
import com.reunet.app.ui.events.EventsActivity
import com.reunet.app.ui.popups.FormAddGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupsActivity : BaseActivity<ActivityGroupsBinding>(ActivityGroupsBinding::inflate) {
    private var TOKEN: String = "TOKEN"

    private lateinit var groups: List<Group>
    private lateinit var recyclerView: RecyclerView

    private val groupService by lazy{
        GroupService.build(intent.getStringExtra(TOKEN).toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.addGroupBtn.setOnClickListener{
            startActivity(Intent(this, FormAddGroup::class.java))
        }
        binding.checkActivities.setOnClickListener{
            startActivity(Intent(this, EventsActivity::class.java))
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
            val call = groupService.getGroups()
            try {
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
            }catch (e: Exception){
                Log.i("Error with coroutines", e.toString())
            }
        }
    }
}