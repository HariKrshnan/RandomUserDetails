package com.hkay.zohouserdetails.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dob(
    @SerialName("age")
    val age: Int?,
    @SerialName("date")
    val date: String?
)