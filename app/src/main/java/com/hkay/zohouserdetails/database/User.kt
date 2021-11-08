package com.hkay.zohouserdetails.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "full_name") val name: String?,
    @ColumnInfo(name = "picture") val picture: String?,
    @ColumnInfo(name = "latitude") val latitude: String?,
    @ColumnInfo(name = "longitude") val longitude: String?
)