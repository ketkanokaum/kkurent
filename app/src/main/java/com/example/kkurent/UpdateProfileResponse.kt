package com.example.kkurent

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateProfileResponse(

    @Expose
    @SerializedName("success")val success: Boolean,

    @Expose
    @SerializedName("message")val message: String
): Parcelable {}