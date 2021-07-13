package com.quan.datn.model.remote

class BaseResponse<T> {
    var status = 0
    var message: String? = null
    var data: T? = null
        private set

    fun setData(data: T) {
        this.data = data
    }
}