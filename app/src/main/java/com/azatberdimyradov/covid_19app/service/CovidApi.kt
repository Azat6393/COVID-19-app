package com.azatberdimyradov.covid_19app.service

import com.azatberdimyradov.covid_19app.model.CommonModel
import io.reactivex.Single
import retrofit2.http.GET

interface CovidApi {

    companion object{
        const val BASE_URL = "https://api.covid19api.com/"
    }

    @GET("summary")
    fun getData(): Single<CommonModel>
}