package com.quan.datn.ui.login

interface LoginCallBack {
    fun getInfoSuccess()
    fun error(err:String?)
    fun phoneNumberNotExists(phoneNumber:String)

}