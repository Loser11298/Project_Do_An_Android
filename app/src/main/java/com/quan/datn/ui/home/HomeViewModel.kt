package com.quan.datn.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.quan.datn.model.BenhAnModel
import com.quan.datn.model.ProfileModel
import com.quan.datn.model.remote.ApiHelp
import com.quan.datn.model.repository.BenhNhanRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    val dsBenhAn = MutableLiveData<MutableList<BenhAnModel>>()

    val repository = ApiHelp.createRetrofit().create(BenhNhanRepository::class.java)


    fun getBenhAn(phoneNumber: String): Disposable? {
        return repository.getBenhAn(phoneNumber).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if (it.status == 200 && it.data?.size ?: 0 > 0){
                        dsBenhAn.value = it.data
                    }
                }, {
                    it.printStackTrace()
                }
            )
    }

}