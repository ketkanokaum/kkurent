package com.example.kkurent

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Register(
    @Expose
    @SerializedName("idUsers") val idUsers : Int,

    @Expose
    @SerializedName("fname_lname") val fname_lname : String,

    @Expose
    @SerializedName("email") val email : String,

    @Expose
    @SerializedName("address") val address : String,

    @Expose
    @SerializedName("bank") val bank : String,

    @Expose
    @SerializedName("account_number") val account_number : String,

    @Expose
    @SerializedName("account_name") val account_name  : String,

    @Expose
    @SerializedName("password") val password : String,

    @Expose
    @SerializedName("phone") val phone : String,

    @Expose
    @SerializedName("profile_picture") val profile_picture : String,

    @Expose
    @SerializedName("id_card_img") val id_card_img : String,

    @Expose
    @SerializedName("role") val role : String,

    @Expose
    @SerializedName("del_status") val del_status :String? = "N"




): Parcelable {}
