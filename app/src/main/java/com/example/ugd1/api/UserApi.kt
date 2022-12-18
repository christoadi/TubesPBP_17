package com.example.ugd1.api

class UserApi {
    companion object{
        val BASE_URL ="http://192.168.0.106/TUBES_API_17/public/api/"

        val GET_ALL_URL = BASE_URL + "profil/"
        val GET_BY_ID_URL = BASE_URL + "profil/"
        val ADD_URL = BASE_URL + "register"
        val UPDATE_URL = BASE_URL + "profil/"
        val DELETE_URL = BASE_URL + "profil/"

        val CHECK = BASE_URL + "login"
    }
}