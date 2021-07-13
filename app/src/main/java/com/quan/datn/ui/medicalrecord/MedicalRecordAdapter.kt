package com.quan.datn.ui.medicalrecord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quan.datn.databinding.ItemTableRowOneBinding

class MedicalRecordAdapter : RecyclerView.Adapter<MedicalRecordAdapter.Companion.ViewHolder> {

    var inter: IMedicalAdapter? = null

    constructor(inter: IMedicalAdapter) {
        this.inter = inter
    }

    companion object {
        class ViewHolder : RecyclerView.ViewHolder {
            var itemTableRowBinding: ItemTableRowOneBinding? = null

            constructor(binding: ItemTableRowOneBinding) : super(binding.root) {
                itemTableRowBinding = binding
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTableRowOneBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.itemTableRowBinding
        if (binding != null) {
               when(holder.itemViewType){
                    0 -> {
                        binding.txtTitle.text = "Mã bệnh án"
                        binding.txtContent.text = inter?.getModel()?.benhAnModel?.value?.maBA
                    }
                   1 -> {
                       binding.txtTitle.text = "Cân Nặng"
                       binding.txtContent.text = "${inter?.getModel()?.benhAnModel?.value?.canNang}"
                   }
                   2 -> {
                       binding.txtTitle.text = "Nhóm Máu"
                       binding.txtContent.text = inter?.getModel()?.benhAnModel?.value?.nhomMau
                   }
                   3 -> {
                       binding.txtTitle.text = "Nhiệt Độ"
                       binding.txtContent.text = "${inter?.getModel()?.benhAnModel?.value?.nhietDo}"
                   }
                   4 -> {
                       binding.txtTitle.text = "Mạch"
                       binding.txtContent.text = "${inter?.getModel()?.benhAnModel?.value?.mach}"
                   }
                   5 -> {
                       binding.txtTitle.text = "Huyết Áp"
                       binding.txtContent.text = "${inter?.getModel()?.benhAnModel?.value?.huyetAp}"
                   }
                   6 -> {
                       binding.txtTitle.text = "Nhịp Thở"
                       binding.txtContent.text = "${inter?.getModel()?.benhAnModel?.value?.nhipTho}"
                   }
                   7 -> {
                       binding.txtTitle.text = "Tình Trạng Hiện Tại"
                       binding.txtContent.text = "${inter?.getModel()?.benhAnModel?.value?.tinhTrangHT}"
                   }
                   8 -> {
                       binding.txtTitle.text = "Lý Do Khám"
                       binding.txtContent.text = "${inter?.getModel()?.benhAnModel?.value?.lyDoKham}"
                   }
                   9 -> {
                       binding.txtTitle.text = "Chẩn Đoán Sơ Bộ"
                       binding.txtContent.text = "${inter?.getModel()?.benhAnModel?.value?.chanDoanSoBo}"
                   }
                   10 -> {
                       binding.txtTitle.text = "Yêu Cầu Thêm"
                       binding.txtContent.text = "${inter?.getModel()?.benhAnModel?.value?.yeuCauThem}"
                   }
                   11 -> {
                       binding.txtTitle.text = "Chẩn Đoán Sau Cùng"
                       binding.txtContent.text = "${inter?.getModel()?.benhAnModel?.value?.chanDoanSauCung}"
                   }
                   12 -> {
                       binding.txtTitle.text = "Hướng Điều Trị"
                       binding.txtContent.text = "${inter?.getModel()?.benhAnModel?.value?.huongDieuTri}"
                   }
                   13 -> {
                       binding.txtTitle.text = "Đơn thuốc"
                       binding.txtContent.text = inter?.getModel()?.benhAnModel?.value?.getDonThuocString()
                   }
               }
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return 14
    }

    interface IMedicalAdapter {
        fun onClickItem(position: Int, view: View)
        fun getModel(): MedicalRecordViewModel
    }

}