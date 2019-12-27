package com.sam43.svginteractiondemo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

object Repository {

    private var service: APIService


    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val intercept = httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY // to check the log
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(intercept)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("") // No base url needed in this project, cz we will download svg image from url
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        service = retrofit.create(APIService::class.java)
    }

    fun downloadFileFromServer(url: String): LiveData<ResponseBody> {
        val responseBodyLD = MutableLiveData<ResponseBody>()
        val apiObject = object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("Some","response found null")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                responseBodyLD.value = response.body()
            }

        }
        service.downloadFileWithDynamicUrlAsync(
            url
        ).enqueue(apiObject)
        return responseBodyLD
    }
}

interface APIService {
    @Streaming
    @GET
    fun downloadFileWithDynamicUrlAsync(
        @Url fileUrl: String
    ): Call<ResponseBody>
}
