package com.example.kkurent


data class Review(
    val idReview: Int,
    val review_comment: String,
    val Rental_idRental: Int,
    val Users_idUsers: Int,
    val Items_idItems: Int
)
