package com.reunet.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ItemSelectGroupBinding
import com.reunet.app.models.Group
import com.reunet.app.ui.event.ItemClickListener

class GroupSelectApadter(
    private val lisGroup: List<Group>,
    private var itemClickListener: ItemClickListener?=null
) : RecyclerView.Adapter<GroupSelectApadter.EventSelectViewHolder>(){




    var selectedPosition = -1

    class EventSelectViewHolder(
        private val binding: ItemSelectGroupBinding
        ):RecyclerView.ViewHolder(binding.root){
        var radioGroup: RadioButton = binding.radioGroup
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventSelectViewHolder {
        val itemSelectActivityBinding = ItemSelectGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventSelectViewHolder(itemSelectActivityBinding)
    }

    override fun onBindViewHolder(holder: EventSelectViewHolder, position: Int) {
        val group: Group = lisGroup[position]


        holder.radioGroup.setText(group.name)
        //Checked selected radio button
        holder.radioGroup.setChecked(position == selectedPosition)
        //Set on listener radio button
        holder.radioGroup.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b){
                //When checked
                //Update selected position
                selectedPosition = holder.adapterPosition
                //Call listener
                itemClickListener?.onClick(group.id)
            }
        })
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        //Pass total list size
        return lisGroup.size
    }
}

