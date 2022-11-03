package com.reunet.app.ui.groups

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ItemGroupBinding
import com.reunet.app.models.Group

class GroupAdapter(
    private val listGroup: List<Group>,
): RecyclerView.Adapter<GroupAdapter.GroupViewHolder>(){
    class GroupViewHolder(
        private val binding: ItemGroupBinding
    ): RecyclerView.ViewHolder(binding.root){
        val groupName:TextView = binding.groupName
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupViewHolder {
        val itemGroupBinding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(itemGroupBinding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group: Group = listGroup[position]
        holder.groupName.text = group.name
    }

    override fun getItemCount(): Int {
        return listGroup.size
    }
}