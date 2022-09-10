package com.example.ugd1.entity

class Workout(
    var jenis: String
) {

    companion object{
        @JvmField
        var userWorkout = arrayOf(
            Workout("Jumping Jacks"),
            Workout("Russian Twist"),
            Workout("Push Up"),
            Workout("Heel Touch"),
            Workout("Plank"),
        )
    }
}