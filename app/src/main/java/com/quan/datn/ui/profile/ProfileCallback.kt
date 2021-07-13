package com.quan.datn.ui.profile

import com.quan.datn.model.ProfileModel

interface ProfileCallback {
    fun updateSuccess(profile:ProfileModel)
    fun updateFalse(err:String?)

}