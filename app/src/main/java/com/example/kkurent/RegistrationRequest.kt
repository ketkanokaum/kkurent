package com.example.kkurent

data class RegistrationRequest(
    val id: Int,
    val name: String,
    val address: String,
    val bank: String,
    val account_number: String,
    val phone_number: String,
    val id_card_img: String,
    val profileImage: String,
    val status: String,
    val date: String
)
