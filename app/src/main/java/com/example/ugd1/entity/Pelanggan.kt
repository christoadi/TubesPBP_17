package com.example.ugd1.entity

class Pelanggan(
    var nama: String, var umur: Int,
    var noTelp: String, var gender: String
) {

    companion object{
        @JvmField
        var userData = arrayOf(
            Pelanggan("Ace Nasutiyon",19,"087816234217","Laki-laki"),
            Pelanggan("Erik Somba",20,"081292742817","Laki-laki"),
            Pelanggan("Aldyo Karnavion",18,"081292816352","Laki-laki"),
            Pelanggan("Ella Margaretta",19,"087791726255","Perempuan"),
            Pelanggan("Vania Aurelia",18,"0877273618239","Perempuan"),
            Pelanggan("Dustin Omar",22,"082212332144","Laki-laki"),
            Pelanggan("Alvin Yoman",17,"081201839481","Laki-laki"),
            Pelanggan("Rafa Markomar",20,"081262518499","Laki-laki"),
            Pelanggan("Chelsea Agatha",21,"081223471923","Perempuan"),
            Pelanggan("Dimas Yeremia",21,"087781729183","Laki-laki"),
        )
    }
}