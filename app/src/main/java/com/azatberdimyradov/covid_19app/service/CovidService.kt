package com.azatberdimyradov.covid_19app.service

import com.azatberdimyradov.covid_19app.model.CommonModel
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CovidService @Inject constructor(private val api: CovidApi) {

    fun getDataFromApi(): Single<CommonModel> = api.getData()

}