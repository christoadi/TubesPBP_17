package com.example.ugd1.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(note: User)
    @Update
    suspend fun updateUser(note: User)
    @Delete
    suspend fun deleteUser(note: User)
    @Query("SELECT * FROM User")
    suspend fun getUser() : List<User>
    @Query("SELECT * FROM User WHERE username =:user_username")
    suspend fun getUser(user_username: String) : List<User>
}