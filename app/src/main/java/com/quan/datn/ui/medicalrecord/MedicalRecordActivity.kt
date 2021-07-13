package com.quan.datn.ui.medicalrecord

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.quan.datn.R
import com.quan.datn.databinding.ActivityMedialRecordBinding
import com.quan.datn.model.BenhAnModel

class MedicalRecordActivity : AppCompatActivity(),
    MedicalRecordAdapter.IMedicalAdapter {

    private lateinit var viewModel: MedicalRecordViewModel
    private lateinit var binding: ActivityMedialRecordBinding

    companion object {
        @JvmStatic
        fun openActivity(context: Context, benhAnModel: BenhAnModel ?= null) {
            val intent = Intent(context, MedicalRecordActivity::class.java)
            intent.putExtra("DATA_SENDER", Gson().toJson(benhAnModel))
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medial_record)
        viewModel = ViewModelProvider(this).get(MedicalRecordViewModel::class.java)

        val data = intent.getStringExtra("DATA_SENDER")
        data?.let {
            viewModel.benhAnModel.postValue(Gson().fromJson(it, BenhAnModel::class.java))
        }

        viewModel.benhAnModel.observe(this, Observer {
            if (binding.listItem.adapter == null) {
                binding.listItem.apply {
                    layoutManager = LinearLayoutManager(this@MedicalRecordActivity)
                    adapter = MedicalRecordAdapter(this@MedicalRecordActivity)
                }
            } else {
                (binding.listItem.adapter as MedicalRecordAdapter).notifyDataSetChanged()
            }
        })

    }

    override fun onClickItem(position: Int, view: View) {

    }

    override fun getModel() = viewModel

}