package com.quan.datn.model.remote

import com.google.gson.*
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.quan.datn.BuildConfig
import com.quan.datn.common.Constants
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object ApiHelp {
    private fun createOkHttp(timeOut: Int = 26): OkHttpClient {
        val client = OkHttpClient.Builder()
        val logging = LoggingInterceptor.Builder()
            .loggable(Constants.DEBUG)
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .request("Request")
            .response("Response")
            .addHeader("version", BuildConfig.VERSION_NAME)
        client.addInterceptor(logging.build())
        return client
            .retryOnConnectionFailure(true)
            .connectTimeout(timeOut.toLong(), TimeUnit.SECONDS)
            .readTimeout(timeOut.toLong(), TimeUnit.SECONDS)
            .writeTimeout(timeOut.toLong(), TimeUnit.SECONDS)
            .build()
    }

    fun createRetrofit(timeOut: Int = 26): Retrofit {
        var gson = GsonBuilder()
        val formatDate = Constants.LIST_FORMAT_TIME
        for (format in formatDate) {
            gson = gson.registerTypeAdapter(Date::class.java, object : JsonDeserializer<Date> {
                override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date {
                    try {
                        return SimpleDateFormat(format, Locale.getDefault()).parse(json!!.asString)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    throw  JsonParseException("Unparseable date: \"" + json!!.asString + "  format: " + format)
                }
            })
        }

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson.create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(createOkHttp(timeOut))
            .build()
    }

}