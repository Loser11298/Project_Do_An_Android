package com.quan.datn.ui.utils

import com.quan.datn.common.Constants

object StringUtils {

    fun String?.isNotNullOrEmpty(f: (it: String) -> Unit) {
        if (this != null && this != "") {
            f(this)
        }
    }

    fun String?.toLink():String {
        return if (this != null && this != "") {
            if (this.contains("http") || this.contains("https")){
                this
            }else{
                Constants.BASE_URL + this
            }
        }else{
            Constants.BASE_URL
        }
    }


    fun isNotBlank(vararg contents: String?): Boolean {
        for (content in contents) {
            if (content == null || content == "") {
                return false
            }
        }
        return true
    }

    fun String?.isPhoneNumber(f: (it: String?) -> Unit) {
        if (this != null && this.length > 9 && this.length < 13) {
            when (this.length) {
                10 -> {
                    if (this.first().toString() == "0") {
                        if (isPhoneVN(this[1]))
                            f(this.replaceFirst("0", "+84"))
                        else
                            f(null)
                    } else
                        f(null)
                }
                11 -> {
                    if (this.first().toString() == "8" && this[1].toString() == "4") {
                        if (isPhoneVN(this[2]))
                            f("+$this")
                        else
                            f(null)
                    } else
                        f(null)
                }
                12 -> {
                    if (this.first().toString() == "+" &&
                        this[1].toString() == "8" &&
                        this[2].toString() == "4"
                    ) {
                        if (isPhoneVN(this[3]))
                            f(this)
                        else
                            f(null)
                    } else
                        f(null)
                }
            }
        }else
            f(null)
    }

    private fun isPhoneVN(phone: Char): Boolean {
        val phoneString = phone.toString()
        if (phoneString == "3" || phoneString == "5" ||
            phoneString == "7" || phoneString == "8" ||
            phoneString == "9"
        ) {
            return true
        }
        return false
    }

}