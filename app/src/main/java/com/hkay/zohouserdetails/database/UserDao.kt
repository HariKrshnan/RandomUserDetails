package com.hkay.zohouserdetails.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getUsers(): List<User>

    @Insert
    suspend fun insertAll(users: List<User>)

    @Delete
    suspend fun deleteAll(users: List<User>)
}