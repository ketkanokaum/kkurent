package com.example.kkurent

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Login(
    @Expose
    @SerializedName("success") val success : Int,

    @Expose
    @SerializedName("idUsers") val idUsers : Int,

    @Expose
    @SerializedName("fname_lname") val fname_lname : String,

    @Expose
    @SerializedName("email") val email : String,
    @Expose
    @SerializedName("password") val password : String,

    @Expose
    @SerializedName("role") val role : String
): Parcelable {
}
