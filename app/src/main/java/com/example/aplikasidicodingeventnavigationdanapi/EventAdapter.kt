package com.example.aplikasidicodingeventnavigationdanapi

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasidicodingeventnavigationdanapi.dataAPI.EventData
import com.example.aplikasidicodingeventnavigationdanapi.databinding.FragmentItemBinding
import com.example.aplikasidicodingeventnavigationdanapi.ui.detail.DetailEvent

class EventAdapter: ListAdapter<EventData, EventAdapter.EventViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventData>() {
            override fun areItemsTheSame(oldItem: EventData, newItem: EventData): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: EventData, newItem: EventData): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailEvent::class.java).apply {
                putExtra("idDetail", event.id)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = currentList.size

    class EventViewHolder(private val binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventData) {
            binding.itemName.text = event.name
            Glide.with(itemView.context).load(event.imageLogo).centerCrop().into(binding.itemImage)
        }
    }
}