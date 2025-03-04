package com.example.kkurent

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteItem(
    @Expose
    @SerializedName("idFavorites") val idFavorites: Int,

    @Expose
    @SerializedName("item_name") val item_name: String,

    @Expose
    @SerializedName("price") val price: String,

    @Expose
    @SerializedName("item_img") val item_img: String,

    @Expose
    @SerializedName("idItems") val idItems: Int
) : Parcelable