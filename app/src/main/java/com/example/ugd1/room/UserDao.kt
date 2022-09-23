package com.example.ugd1.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun addUser(note: User)
    @Update
    fun updateUser(note: User)
    @Delete
    fun deleteUser(note: User)
    @Query("SELECT * FROM User")
    fun getUser() : List<User>
    @Query("SELECT * FROM User WHERE username =:user_username")
    fun getUser(user_username: String) : User
    @Query("SELECT * FROM User WHERE username =:username AND password = :password")
    fun checkUser(username: String, password: String): User?
}