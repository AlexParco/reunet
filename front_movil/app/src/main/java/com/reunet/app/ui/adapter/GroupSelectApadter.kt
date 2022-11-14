package com.reunet.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.R
import com.reunet.app.databinding.ItemSelectGroupBinding
import com.reunet.app.models.Group
import com.reunet.app.ui.event.ItemClickListener

class GroupSelectApadter(
    private val lisGroup: List<Group>,
    private var itemClickListener: ItemClickListener?=null
) : RecyclerView.Adapter<GroupSelectApadter.EventSelectViewHolder>(){
    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventSelectViewHolder {
        val itemSelectActivityBinding = parent.inflate(R.layout.item_select_group)
        return EventSelectViewHolder(itemSelectActivityBinding)
    }

    override fun onBindViewHolder(holder: EventSelectViewHolder, position: Int) {
        val group: Group = lisGroup[position]
        val context = holder.itemView.context
        holder.bind(group, context, position)
    }

    inner class EventSelectViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val binding = ItemSelectGroupBinding.bind(view)
        fun bind(group: Group, context: Context, position: Int){
            with(binding) {
                radioGroup.setText(group.name)
                radioGroup.setChecked(position == selectedPosition)
                radioGroup.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
                    if (b){
                        //When checked
                        //Update selected position
                        selectedPosition = adapterPosition
                        //Call listener
                        itemClickListener?.onClick(group.id)
                    }
                })
            }
        }
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

