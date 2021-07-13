package com.quan.datn.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.quan.datn.databinding.*


class HomeAdapter : RecyclerView.Adapter<HomeAdapter.Companion.ViewHolder> {

    var inter:IHomeAdapter? = null

    constructor(inter:IHomeAdapter){
        this.inter = inter
    }

    companion object {
        class ViewHolder : RecyclerView.ViewHolder {
            var itemHeaderBinding : ItemHeaderHistoryBinding ?= null

            constructor(binding: ItemHeaderHistoryBinding) : super(binding.root){
                itemHeaderBinding = binding
            }

            var itemRowHistoryBinding:ItemRowHistoryBinding ?= null

            constructor(binding: ItemRowHistoryBinding) : super(binding.root){
                itemRowHistoryBinding = binding
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding
        return when (viewType) {
            0 -> {
                binding = ItemHeaderHistoryBinding.inflate(inflater, parent, false)
                ViewHolder(binding)
            }
            else -> {
                binding = ItemRowHistoryBinding.inflate(inflater, parent, false)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.itemViewType == 0){
            val binding = holder.itemHeaderBinding
            binding?.executePendingBindings()
        } else {
            val binding = holder.itemRowHistoryBinding
            if (binding != null) {
                binding.benhAn = inter?.getModel()?.dsBenhAn?.value!![position - 1]
                binding.stt.text = "${position - 1}"
                binding.onClickRow.setOnClickListener {
                    inter?.onClickItem(position - 1, binding.onClickRow)
                }
            }
            binding?.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return if (inter?.getModel()?.dsBenhAn?.value == null)
            1
        else
            inter?.getModel()?.dsBenhAn?.value!!.size + 1
    }

    interface IHomeAdapter {
        fun onClickItem(position: Int,view:View)
        fun getModel():HomeViewModel
    }

}