package com.hkay.zohouserdetails.model.rickandmorty

import com.google.gson.annotations.SerializedName

data class PagedResponse<T>(
    @SerializedName("info") val pageInfo: PageInfo,
    val results: List<Characters> = listOf()
)

data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String?
)


data class Characters(
    val id: Int,
    val name: String,
    val status: Status,
    val species: String,
    val type: String,
    val gender: Gender,
    val image: String,
    val url: String,
    val origin: NameUrl,
    val location: NameUrl,
    val episode: List<String>
)
enum class Status(val status: String) {
    @SerializedName(value = "Alive", alternate = ["alive"])
    ALIVE("Alive"),

    @SerializedName(value = "Dead", alternate = ["dead"])
    DEAD("Dead"),

    @SerializedName(value = "unknown", alternate = ["Unknown"])
    UNKNOWN("Unknown");

    override fun toString() = status
}

enum class Gender(val gender: String) {
    @SerializedName(value = "Female", alternate = ["female"])
    FEMALE("Female"),

    @SerializedName(value = "Male", alternate = ["male"])
    MALE("Male"),

    @SerializedName(value = "Genderless", alternate = ["genderless"])
    GENDERLESS("Genderless"),

    @SerializedName(value = "unknown", alternate = ["Unknown"])
    UNKNOWN("Unknown");

    override fun toString() = gender
}

