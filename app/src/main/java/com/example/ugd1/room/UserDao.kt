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
    @Query("SELECT * FROM User WHERE name =:user_name")
    suspend fun getUser(user_name: String) : List<User>
    @Query("SELECT * FROM User WHERE number =:user_number")
    suspend fun getUser(user_number: Int) : List<User>

}