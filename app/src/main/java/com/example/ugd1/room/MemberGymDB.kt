package com.example.ugd1.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [MemberGym::class],
    version = 1
)

abstract class MemberGymDB: RoomDatabase() {
    abstract fun MemberGymDao() : MemberGymDao
    companion object {
        @Volatile private var instance : MemberGymDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            MemberGymDB::class.java,
            "membergym12345.db"
        ).build()
    }
}