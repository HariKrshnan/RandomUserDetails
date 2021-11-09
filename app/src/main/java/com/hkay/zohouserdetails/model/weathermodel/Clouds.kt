package com.hkay.zohouserdetails.model.weathermodel


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int?
)