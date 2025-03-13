package com.example.kkurent


import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(

   @Expose
   @SerializedName("idItems") val idItems: Int,

   @Expose
   @SerializedName("item_name") val item_name: String,

   @Expose
   @SerializedName("item_detail") val item_detail: String,

   @Expose
   @SerializedName("price") val price: Int,

   @Expose
   @SerializedName("item_img") val item_img: String,

   @Expose
   @SerializedName("location") val location: String,

   @Expose
   @SerializedName("fname_lname") val fname_lname: String,

   @Expose
   @SerializedName("category_name") val category_name: String,

   @Expose
   @SerializedName("item_status") val item_status: String,

   @Expose
   @SerializedName("user_id") val user_id: Int,

   @Expose
   @SerializedName("profile_picture") val profilePicture: String,

   @Expose
   @SerializedName("createAt") val createAt: String,

   @Expose
   @SerializedName("deleteAt") val deleteAt: String
   ) : Parcelable {

}
