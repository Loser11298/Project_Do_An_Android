package com.quan.datn.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.quan.datn.MainActivity
import com.quan.datn.R
import com.quan.datn.databinding.FragmentHomeBinding
import com.quan.datn.ui.medicalrecord.MedicalRecordActivity
import com.quan.datn.ui.utils.DataManager

class HomeFragment : Fragment(), HomeAdapter.IHomeAdapter {
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding:FragmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        DataManager.getInfoSessionLogin(this.requireContext())?.let {
            viewModel.getBenhAn(it.sdt)
        }

        binding.listItem.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.requireContext())
            adapter = HomeAdapter(this@HomeFragment)
        }

        viewModel.dsBenhAn.observe(viewLifecycleOwner, Observer {
            (binding.listItem.adapter as HomeAdapter).notifyDataSetChanged()
        })

        return binding.root
    }

    override fun onClickItem(position: Int, view: View) {
        val benhAn = viewModel.dsBenhAn.value!![position]
        MedicalRecordActivity.openActivity(this.requireContext(), benhAn)
    }

    override fun getModel() = viewModel

}