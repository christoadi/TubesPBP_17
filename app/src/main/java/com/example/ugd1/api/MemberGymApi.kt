package com.example.ugd1.api

class MemberGymApi {
    companion object{
        val BASE_URL ="http://192.168.0.111/API_TUBES_17/public/"

        val GET_ALL_URL = BASE_URL + "memberGym/"
        val GET_BY_ID_URL = BASE_URL + "memberGym/"
        val ADD_URL = BASE_URL + "memberGym"
        val UPDATE_URL = BASE_URL + "memberGym/"
        val DELETE_URL = BASE_URL + "memberGym/"
    }
}