package com.quan.datn.ui.utils

import android.content.Context
import com.google.gson.Gson
import com.quan.datn.model.ProfileModel

object DataManager {

    fun savePhoneLogin(context:Context, phoneNumber:String){
        val sharedPreferences = context.getSharedPreferences("data_datn", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("PHONE_LOGIN",phoneNumber )
        editor.apply()
    }

    fun getPhoneLogin(context:Context):String {
        val sharedPreferences = context.getSharedPreferences("data_datn", Context.MODE_PRIVATE)
        if (sharedPreferences != null) {
            return sharedPreferences.getString("PHONE_LOGIN", "").toString()
        }
        return ""
    }

    fun getInfoSessionLogin(context: Context): ProfileModel? {
        val sharedPreferences = context.getSharedPreferences("data_datn", Context.MODE_PRIVATE)
        if (sharedPreferences != null) {
            val data  = sharedPreferences.getString("INFO_BN", "").toString()
            return Gson().fromJson(data, ProfileModel::class.java)
        }
        return null
    }

    fun saveSessionLogin(context: Context, profile:ProfileModel){
        val sharedPreferences = context.getSharedPreferences("data_datn", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("INFO_BN", Gson().toJson(profile))
        editor.apply()
    }

    fun removeLoginSession(context:Context){
        val sharedPreferences = context.getSharedPreferences("data_datn", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("PHONE_LOGIN")
        editor.remove("INFO_BN")
        editor.apply()
    }



}