package com.quan.datn.ui.utils

import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.quan.datn.R
import com.quan.datn.ui.utils.StringUtils.toLink

class LoadDataBinding {
    companion object {

        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(iv: ImageView, link: String?) {
            if (!StringUtils.isNotBlank(link)){
                Glide.with(iv)
                    .load(R.drawable.avatar)
                    .into(iv)
            } else {
                Glide.with(iv)
                    .load(link.toLink())
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(iv)
            }
        }

        @JvmStatic
        @BindingAdapter("updateText")
        fun updateText(tv: TextView, value: String?) {
            tv.text = value
        }

        @JvmStatic
        @BindingAdapter("updateTextInt")
        fun updateTextInt(tv: TextView, value: Int?) {
            tv.text = "$value"
        }

        @JvmStatic
        @BindingAdapter("updateTextFloat")
        fun updateTextFloat(tv: TextView, value: Float?) {
            tv.text = "$value"
        }

        @JvmStatic
        @BindingAdapter("updateGenderNam")
        fun updateGenderNam(rb: RadioButton, value: String?) {
            if (value != null) {
                rb.isChecked = value.contains("Nam")
            }
        }

        @JvmStatic
        @BindingAdapter("updateGenderNu")
        fun updateGenderNu(rb: RadioButton, value: String?) {
            if (value != null) {
                rb.isChecked = value.contains("Nữ")
            }
        }


    }
}