package com.example.ugd1.room

import androidx.room.*

@Dao
interface MemberGymDao {
    @Insert
    fun addMemberGym(note: MemberGym)

    @Update
    fun updateMemberGym(note: MemberGym)

    @Delete
    fun deleteMemberGym(note: MemberGym)

    @Query("SELECT * FROM MemberGym")
    suspend fun getMemberGym() : List<MemberGym>

    @Query("SELECT * FROM MemberGym WHERE id =:user_id")
    suspend fun getMemberGym(user_id: Int) : List<MemberGym>
}