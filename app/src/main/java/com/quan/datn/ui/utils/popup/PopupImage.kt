package com.quan.datn.ui.utils.popup

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import com.quan.datn.R

class PopupImage : PopupMenu {

    private var mInter:IPopupChooseImage ?= null

    constructor(context: Context, view: View, inter:IPopupChooseImage) : super(context,view) {
        mInter = inter
        inits()
    }

    private fun inits(){
        menuInflater.inflate(R.menu.pick_image, menu)
        setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.viewAvatar -> {
                    mInter?.viewImage()
                    true
                }
                R.id.library -> {
                    mInter?.chooseLibrary()
                    true
                }
                else -> false
            }
        }
    }

    interface IPopupChooseImage {
        fun chooseLibrary()
        fun viewImage()
    }

}