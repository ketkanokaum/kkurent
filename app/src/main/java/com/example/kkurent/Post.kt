package com.example.kkurent

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(

    @Expose
    @SerializedName("idPost") val idPost: Int,
    @Expose
    @SerializedName("post_detail") val post_detail: String,
    @Expose
    @SerializedName("post_img") val post_img: String,

    @Expose
    @SerializedName("user_id") val user_id: Int,
    @Expose
    @SerializedName("fname_lname") val fname_lname: String,
//    @Expose
//    @SerializedName("profile_picture") val profile_picture: String

    ): Parcelable {}
