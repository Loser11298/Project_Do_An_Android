package com.quan.datn.model

data class DonThuocModel  (
    var id: Int,
    var maBA: String,
    var tenThuoc: String,
    var soLuong:Int,
    var donGia: Float,
    var thanhTien:Float,
    var huongDan:String
){

    fun getString():String {
        return "$tenThuoc: $huongDan"
    }

}