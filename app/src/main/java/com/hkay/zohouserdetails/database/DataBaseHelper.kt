package com.hkay.zohouserdetails.database

interface DataBaseHelper {
    suspend fun getUsers(): List<User>
    suspend fun insertAll(users: List<User>)
}