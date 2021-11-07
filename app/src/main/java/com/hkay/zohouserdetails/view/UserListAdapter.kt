package com.hkay.zohouserdetails.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hkay.zohouserdetails.databinding.UserListItemBinding
import com.hkay.zohouserdetails.model.Result

class UserListAdapter(context: Context, private val listener: ((Result?) -> Unit)? = null) :
    androidx.recyclerview.widget.ListAdapter<Result, RecyclerView.ViewHolder>(UserDiffCallBack()) {

    val localContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i("Length", position.toString())
        (holder as UserListViewHolder).bind(getItem(position))
    }

    inner class UserListViewHolder(private val binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Result) {
            Glide.with(localContext)
                .load(item.picture?.medium)
                .into(binding.itemImageView)
            binding.itemTextView.text = item.name?.first + " " + item.name?.last
            Log.e("List", item.name?.first + " " + item.name?.last)
            binding.root.setOnClickListener {
                listener?.invoke(item)
            }
        }
    }

    class UserDiffCallBack : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Result, newItem: Result) = oldItem == newItem
    }
}