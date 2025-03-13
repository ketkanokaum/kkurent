package com.example.kkurent.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    @Expose
    @SerializedName("id") val id: Int,

    @Expose
    @SerializedName("otherUserId") val otherUserId: Int,

    @Expose
    @SerializedName("image") val image: String,

    @Expose
    @SerializedName("name") val name: String,

    @Expose
    @SerializedName("message_text") val messageText: String,

    @Expose
    @SerializedName("createAt") val createdAt: String,
) : Parcelable

@Parcelize
data class Message(
    @Expose
    @SerializedName("id") val id: Int,

    @Expose
    @SerializedName("sender_id") val senderId: Int,

    @Expose
    @SerializedName("sender_profile_picture") val senderProfilePicture: String,

    @Expose
    @SerializedName("receiver_profile_picture") val receiverProfilePicture: String,

    @Expose
    @SerializedName("sender_fname_lname") val senderFnameLname: String,

    @Expose
    @SerializedName("receiver_fname_lname") val receiverFnameLname: String,

    @Expose
    @SerializedName("message_text") val messageText: String,

    @Expose
    @SerializedName("createAt") val createdAt: String
) : Parcelable


