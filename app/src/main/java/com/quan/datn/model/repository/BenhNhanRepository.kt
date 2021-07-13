package com.quan.datn.model.repository

import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.quan.datn.model.BenhAnModel
import com.quan.datn.model.ProfileModel
import com.quan.datn.model.remote.BaseResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface BenhNhanRepository {

    @GET(value = "/api/benhnhan/get-info")
    fun getInfo(
        @Query("phoneNumber") phoneNumber:String
    ): Observable<BaseResponse<ProfileModel>>

    @GET(value = "/api/benhnhan/check-phone-number")
    fun checkPhoneNumber(
        @Query("phoneNumber") phoneNumber:String
    ): Observable<BaseResponse<String>>

    @GET(value = "/api/benhan/all-benh-an")
    fun getBenhAn(
        @Query("phoneNumber") phoneNumber:String
    ): Observable<BaseResponse<MutableList<BenhAnModel>>>

    @Multipart
    @POST(value = "/api/benhnhan/update")
    fun updateInfo(
        @Part file: MultipartBody.Part?,
        @Part("profile") profile:String
    ):Observable<BaseResponse<ProfileModel>>

}