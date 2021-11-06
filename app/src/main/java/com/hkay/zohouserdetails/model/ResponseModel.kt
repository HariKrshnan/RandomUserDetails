package com.hkay.zohouserdetails.model

import com.google.gson.annotations.SerializedName


data class ResponseModel(
    @SerializedName("info")
    val info: Info?,
    @SerializedName("results")
    val results: List<Result>?
)