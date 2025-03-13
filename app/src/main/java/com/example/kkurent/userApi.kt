package com.example.kkurent

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface userApi {
    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Login>

    @GET("search/{idUsers}")
    fun searchUser(
        @Path("idUsers") idUsers: Int,
    ): Call<Register>

    @Multipart
    @POST("insertAccount")
    fun insertUser(
        @Part("idUsers") idUsers: RequestBody,  // ✅ เพิ่มฟิลด์ idUsers ที่หายไป
        @Part("fname_lname") fname_lname: RequestBody,
        @Part("email") email: RequestBody,
        @Part("address") address: RequestBody,
        @Part("bank") bank: RequestBody,
        @Part("account_number") account_number: RequestBody,
        @Part("account_name") account_name: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part profile_picture: MultipartBody.Part?,  // ✅ ย้ายลงมาตามลำดับ Backend
        @Part id_card_img: MultipartBody.Part?,  // ✅ ย้ายลงมาตามลำดับ Backend
        @Part("role") role: RequestBody
    ): Call<Register>


    @GET("allUsers")
    fun showAllUsers(): Call<List<Register>>

    @GET("searchUsers")
    fun searchUsers(
        @Query("fname_lname") fname_lname: String
    ): Call<List<Register>>

    //ลบนUsers
    @PUT("delete_softUsers/{idUsers}") /// Soft Delete
    fun delete_softUsers(
        @Path("idUsers") idUsers: Int
    ): Call<Register>

    //แสดงนUsersที่ถูกลบ
    @GET("deletedusers")
    fun deletedusers(): Call<Map<String, Any>>

    // กู้คืนUsers
    @PUT("restoreUsers/{idUsers}")
    fun restoreUsers(
        @Path("idUsers") idUsers: Int
    ): Call<Register>

    // check email exists
    @GET("checkEmail/{email}")
    fun checkEmail(
        @Path("email") email: String
    ): Call<Boolean>

    companion object {
        fun create(): userApi {
            val userClient = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:4000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(userApi::class.java)
            return userClient
        }
    }
}

