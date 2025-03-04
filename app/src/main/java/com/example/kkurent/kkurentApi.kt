package com.example.kkurent

import com.example.kkurent.data.Chat
import com.example.kkurent.data.Message
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface kkurentApi {
    @GET("allpost")
    fun retrievePost(): Call<List<Post>>

    @GET("allpost/{user_id}")
    fun retrieveUserPosts(
        @Path("user_id") user_id: Int,
    ): Call<List<Post>>



    @FormUrlEncoded
    @POST("post")
    fun insertPost(
        @Field("idPost") idPost: Int,
        @Field("post_detail") post_detail: String,
        @Field("post_img") post_img: String

    ): Call<Post>

    @FormUrlEncoded /// Update
    @PUT("updatePost/{idPost}")
    fun update_post(
        @Path("idPost") id: Int,
        @Field("post_detail") post_detail: String,
        @Field("post_img") post_img: String
    ): Call<Post>

    @Multipart /// Insert and Upload Image
    @POST("insertUpload")
    fun uploadPost(
//        @Part("idPost") idPost: Int,
        @Part("post_detail") post_detail: RequestBody,
        @Part post_img: MultipartBody.Part,
//        @Part("fname_lname") fname_lname: RequestBody
        @Part("user_id") userId: RequestBody

    ): Call<Post>

    @GET("searchPost")
    fun searchPost(
        @Query("post_detail") post_detail: String
    ): Call<List<Post>>

    @GET("searchItems")
    fun searchItems(
        @Query("item_name") item_name: String
    ): Call<List<Item>>

    @Multipart
    @PUT("editUploadPost/{idPost}")
    fun edit_post(
        @Path("idPost") idPost: Int,
        @Part("post_detail") post_detail: RequestBody,
        @Part post_img: MultipartBody.Part?
    ): Call<Post>


    @GET("allitems")
    fun retrieveItems(): Call<List<Item>>


    @GET("allitems/{user_id}")
    fun retrieveUserProducts(@Path("user_id") user_id: Int): Call<List<Item>>


    @Multipart
    @POST("insertItems")
    fun insertItems(
        @Part image: MultipartBody.Part,
        @Part("idItems") idItems: RequestBody,
        @Part("item_name") item_name: RequestBody,
        @Part("item_detail") item_detail: RequestBody,
        @Part("price") price: RequestBody,
        @Part("location") location: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part("category_name") category_name: RequestBody,
        @Part("item_status") item_status: RequestBody
    ): Call<Item>

    @FormUrlEncoded /// Update
    @PUT("updateItem/{idItems}")
    fun updateItems(
        @Part image: String,
        @Part("idItems") idItems: Int,
        @Part("item_name") item_name: String,
        @Part("item_detail") item_detail: String,
        @Part("price") price: Int,
        @Part("location") location: String,
        @Part("category_name") category_name: String
//    @Part("item_status") item_status: String
    ): Call<Item>

    @Multipart
    @PUT("editupdateItem/{idItems}")
    fun editItems(
        @Path("idItems") idItems: Int,
        @Part image: MultipartBody.Part?,
        @Part("item_name") item_name: RequestBody,
        @Part("item_detail") item_detail: RequestBody,
        @Part("price") price: RequestBody,
        @Part("location") location: RequestBody,
        @Part("category_name") category_name: RequestBody,
//        @Part("user_id") user_id: RequestBody,
        @Part("item_status") item_status: RequestBody
    ): Call<Item>

    @GET("registration_requests")
    fun getRegistrationRequests(): Call<List<RegistrationRequest>>

    @FormUrlEncoded
    @POST("registration_requests")
    fun createRegistrationRequest(
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("bank") bank: String,
        @Field("account_number") accountNumber: String,
        @Field("phone_number") phoneNumber: String,
        @Field("id_card_img") idCardImg: String
    ): Call<RegistrationRequest>

    @DELETE("registration_requests/{id}")
    fun deleteRegistrationRequest(
        @Path("id") id: Int
    ): Call<Void>

    @FormUrlEncoded
    @PUT("registration_requests/{id}")
    fun updateRegistrationStatus(
        @Path("id") id: Int,
        @Field("status") status: String
    ): Call<RegistrationRequest>

    @Multipart
    @POST("updateProfile")
    fun updateProfile(
        @Part("idUsers") idUsers: RequestBody,  // ✅ เพิ่มฟิลด์ idUsers ที่หายไป
        @Part("fname_lname") fname_lname: RequestBody,
        @Part("email") email: RequestBody,
        @Part("address") address: RequestBody,
        @Part("bank") bank: RequestBody,
        @Part("accountNumber") accountNumber: RequestBody,
        @Part("accountName") accountName: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part profile_picture: MultipartBody.Part?,  // ✅ ย้ายลงมาตามลำดับ Backend
        @Part id_card_img: MultipartBody.Part?,  // ✅ ย้ายลงมาตามลำดับ Backend
    ): Call<UpdateProfileResponse>

    @GET("detail/{idItems}")
    fun datailItems(
        @Path("idItems") idItems: Int,
    ): Call<Item>

    // /addFavorite
    // ใช้สำหรับเพิ่มลงสินค้าโปรด
    @FormUrlEncoded
    @POST("addFavorite")
    fun addFavorite(
        @Field("idItems") idItems: Int,
        @Field("idUsers") idUsers: Int
    ): Call<FavoriteItem>

    // /deleteFavorite/{idFavorites}
    // ใช้สำหรับลบออกจากสินค้าโปรด
    @DELETE("deleteFavorite/{idFavorites}")
    fun deleteFavorite(
        @Path("idFavorites") idItems: Int
    ): Call<FavoriteItem>

    // /favorite/{idUsers}
    // ใช้สำหรับดึงสินค้าโปรดทั้งหมดของ idUsers ที่ส่งมา
    @GET("favorite/{idUsers}")
    fun getFavorite(@Path("idUsers") idUsers: Int): Call<List<FavoriteItem>>


    @PUT("updateItemStatus/{idItems}")
    fun updateItemStatus(
        @Path("idItems") idItems: Int,
        @Body request: UpdateStatusRequest
    ): Call<Item>


    //ลบโพสต์ตามหาสินค้า
    @PUT("delete_softPost/{idPost}") /// Soft Delete
    fun softDeletePost(
        @Path("idPost") idPost: Int
    ): Call<Post>


    //ลบโพสต์ปล่อยเช่าสินค้า
    @PUT("delete_softItems/{idItems}") /// Soft Delete
    fun softDeleteItems(
        @Path("idItems") idItems: Int
    ): Call<Item>


    //แสดงโพสต์ที่ถูกลบ
    @GET("deletedposts")
    fun deletedposts(): Call<Map<String, Any>>

    // ดึงรายการสินค้าที่รออนุมัติ
    @GET("needapproveitems")
    fun needapproveitems(): Call<Map<String, Any>>

    // ดึงรายการโพสต์ที่รออนุมัติ
    @GET("needapproveposts")
    fun needapproveposts(): Call<Map<String, Any>>

    // อนุมัติโพสต์
    @PUT("approvePost/{postId}")
    fun approvePost(
        @Path("postId") postId: Int
    ): Call<Post>

    // ไม่อนุมัติโพสต์
    @DELETE("disapprovePost/{postId}")
    fun disapprovePost(
        @Path("postId") postId: Int
    ): Call<Post>

    // อนุมัติสินค้า
    @PUT("approveProduct/{productId}")
    fun approveProduct(
        @Path("productId") productId: Int
    ): Call<Item>

    // ไม่อนุมัติสินค้า
    @DELETE("disapproveProduct/{productId}")
    fun disapproveProduct(
        @Path("productId") productId: Int
    ): Call<Item>

    // กู้คืนโพสต์
    @PUT("restorePost/{postId}")
    fun restorePost(
        @Path("postId") postId: Int
    ): Call<Post>

    // กู้คืนสินค้า
    @PUT("restoreProduct/{productId}")
    fun restoreProduct(
        @Path("productId") productId: Int
    ): Call<Item>

    // ดึงรายการสินค้าที่ถูกลบ
    @GET("deleteditems")
    fun deletedItems(): Call<Map<String, Any>>

    // == /chat ==

    // เส้นดึงแชททั้งหมดของผู้ใช้
    @GET("chats/{userId}")
    fun getChats(
        @Path("userId") userId: Int
    ): Call<List<Chat>>

    // เส้นดึงข้อความทั้งหมดในการคุยกับคนคนนึง
    @GET("chats/{userId}/{otherUserId}")
    fun getMessages(
        @Path("userId") userId: Int,
        @Path("otherUserId") otherUserId: Int
    ): Call<List<Message>>

    // เส้นสร้างข้อความ
    @FormUrlEncoded
    @POST("chats")
    fun sendMessage(
        @Field("sender_id") senderId: Int,
        @Field("receiver_id") receiverId: Int,
        @Field("message_text") messageText: String
    ): Call<Message>

    // เส้นดึงข้อมูลคนที่คุยด้วย
    @GET("chatheader/{userId}")
    fun getChatheader(
        @Path("userId") userId: Int,
    ): Call<ChatHeader>

    @GET("chatsentinfo/{userId}")
    fun getChatSentInfo(
        @Path("userId") userId: Int,
    ): Call<ChatSendInfo>

    // == chat\ ==

    // == /notification ==
    @GET("notifications/{userId}")
    fun getNotifications(
        @Path("userId") userId: Int,
    ): Call<List<Notification>>

    @FormUrlEncoded
    @POST("notifications")
    fun createNotification(
        @Field("idUsers") userId: Int,
        @Field("title") title: String,
        @Field("message") message: String,
    ): Call<Notification>

    @PUT("notifications/{notificationId}")
    fun readNotification(
        @Path("notificationId") notificationId: Int,
    ): Call<Notification>

    @GET("notifications/{userId}/unread-count")
    fun getUnreadNotificationCount(
        @Path("userId") userId: Int
    ): Call<NotificationCount>
    // == notification\ ==

    companion object {
        fun create(): kkurentApi {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(kkurentApi::class.java)
        }
    }
}