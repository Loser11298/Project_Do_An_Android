package com.quan.datn.model

import java.lang.StringBuilder

data class BenhAnModel (
    var id : Int,
    var maBA :String?,
    var canNang : Float?,
    var nhomMau:String?,
    var nhietDo: Float?,
    var mach: Int?,
    var huyetAp: Int?,
    var nhipTho: Int?,
    var lyDoKham:String?,
    var tinhTrangHT:String?,
    var benhSu:String?,
    var chanDoanSoBo:String?,
    var yeuCauThem:String?,
    var chanDoanSauCung:String?,
    var huongDieuTri:String?,
    var thoiGianKham :String?,
    var phongKham : PhongKhamModel?,
    var phongBenh: PhongBenhModel?,
    var donThuoc : MutableList<DonThuocModel>? = arrayListOf()
){

    fun getDonThuocString():String {
        if (donThuoc != null && donThuoc?.size ?: 0 > 0){
            val content = StringBuilder()
            for (item in donThuoc!!){
                content.append(item.getString()+"\n")
            }
            return content.toString()
        }
        return ""
    }

}
