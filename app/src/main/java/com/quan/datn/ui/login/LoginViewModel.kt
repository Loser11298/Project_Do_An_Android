package com.quan.datn.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.quan.datn.model.remote.ApiHelp
import com.quan.datn.model.repository.BenhNhanRepository
import com.quan.datn.ui.utils.DataManager
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel : ViewModel() {

    var callBack: LoginCallBack? = null

    private val repository: BenhNhanRepository =
        ApiHelp.createRetrofit().create(BenhNhanRepository::class.java)

    fun getInfoBenhNhan(content: Context): Disposable {
        return repository.getInfo(DataManager.getPhoneLogin(content))
            .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.status == 200 && it.data != null) {
                        Observable.create((ObservableOnSubscribe<Boolean> { _ ->
                            DataManager.saveSessionLogin(content, it.data!!)
                        })).subscribeOn(Schedulers.newThread()).subscribe()
                        callBack?.getInfoSuccess()
                    } else {
                        callBack?.error(it.message)
                    }
                }, {
                    it.printStackTrace()
                    callBack?.error(it.localizedMessage)
                }
            )
    }

    fun checkPhoneNumber(phoneNumber: String): Disposable {
        return repository.checkPhoneNumber(phoneNumber)
            .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.status == 200) {
                        callBack?.phoneNumberNotExists(phoneNumber)
                    } else {
                        callBack?.error(it.message)
                    }
                }, {
                    it.printStackTrace()
                    callBack?.error(it.localizedMessage)
                }
            )
    }

}