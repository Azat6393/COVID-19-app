package com.azatberdimyradov.covid_19app.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommonModel(
    @SerializedName("Global")
    val global: Global,
    @SerializedName("Countries")
    val countries: List<Country>,
) : Parcelable {

    @Parcelize
    data class Country(
        @SerializedName("Country")
        val countryName: String,
        @SerializedName("NewConfirmed")
        val newConfirmed: String,
        @SerializedName("TotalConfirmed")
        val totalConfirmed: String,
        @SerializedName("NewDeaths")
        val newDeaths: String,
        @SerializedName("TotalDeaths")
        val totalDeaths: String,
        @SerializedName("TotalRecovered")
        val totalRecovered: String,
        @SerializedName("NewRecovered")
        val newRecovered: String
    ) : Parcelable

    @Parcelize
    data class Global(
        @SerializedName("NewConfirmed")
        val newConfirmed: String,
        @SerializedName("TotalConfirmed")
        val totalConfirmed: String,
        @SerializedName("NewDeaths")
        val newDeaths: String,
        @SerializedName("TotalDeaths")
        val totalDeaths: String,
        @SerializedName("NewRecovered")
        val newRecovered: String,
        @SerializedName("TotalRecovered")
        val totalRecovered: String
    ) : Parcelable

}
