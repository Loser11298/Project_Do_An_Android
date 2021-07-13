package com.quan.datn.ui.profile

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.quan.datn.model.ProfileModel
import com.quan.datn.model.UpdateInfoRequest
import com.quan.datn.model.remote.ApiHelp
import com.quan.datn.model.repository.BenhNhanRepository
import com.quan.datn.ui.utils.DataManager
import com.quan.datn.ui.utils.StringUtils.isNotNullOrEmpty
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileViewModel : ViewModel() {

    val userProfile = MutableLiveData<ProfileModel>()
    var callBack:ProfileCallback? = null

    private val repository: BenhNhanRepository =
        ApiHelp.createRetrofit().create(BenhNhanRepository::class.java)

    @SuppressLint("CheckResult")
    fun updateFile(updateInfoRequest: UpdateInfoRequest, pathFile:String) {

        var updateAvatarRequest: MultipartBody.Part? = null
        val profileUpdate =  Gson().toJson(updateInfoRequest)
        pathFile.isNotNullOrEmpty {
            val file = File(pathFile)
            val requestFile: RequestBody =
                RequestBody.create("*/*".toMediaTypeOrNull(), file)
            updateAvatarRequest =
                MultipartBody.Part.createFormData("avatar", file.name, requestFile)
        }
        repository.updateInfo(updateAvatarRequest,profileUpdate).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.status == 200 && it.data != null) {
                    callBack?.updateSuccess(it.data!!)
                    userProfile.value = it.data
                }else{
                    callBack?.updateFalse(it.message)
                }
            }, {
                callBack?.updateFalse(it.localizedMessage)
                it.printStackTrace()
            })
    }


}