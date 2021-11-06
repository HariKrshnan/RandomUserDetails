package com.hkay.zohouserdetails.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseModel(
    @SerialName("info")
    val info: Info?,
    @SerialName("results")
    val results: List<Result>?
)