package com.example.kkurent


import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import android.content.Context
import androidx.compose.foundation.layout.offset


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(navController: NavHostController) {
    val context = LocalContext.current
    val initialUser = Register(0, "", "", "",  "", "" , "", "", "", "",  "" ,  " ","") // ข้อมูลเริ่มต้น
    var userItems by remember { mutableStateOf(initialUser) }
    var expanded by remember { mutableStateOf(false) }
    val contextForToast = LocalContext.current
    var user_id by remember { mutableIntStateOf(0) }
    var idPost by remember { mutableIntStateOf(0) }
    var post_detail by remember { mutableStateOf("") }
    var post_img by remember { mutableStateOf<Uri?>(null) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    /// Get Uri
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
            }
        }
    )

    Column {
        CenterAlignedTopAppBar(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart =  16.dp, bottomEnd = 16.dp))
                .fillMaxWidth()
                .height(105.dp),
            title = {
                Text(text = "สร้างโพสของคุณ", color = Color.White, modifier = Modifier.offset(y = 10.dp))
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF003366)
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                value = post_detail,
                onValueChange = { post_detail = it },
                label = { Text(text = "คุณต้องการหาอะไร....") },
                maxLines = 10
            )
            Spacer(modifier = Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(30.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = if (imageUri != null) {
                        rememberAsyncImagePainter(imageUri)
                    } else {
                        painterResource(id = R.drawable.image_search)
                    },
                    contentDescription = null,
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            // ปุ่มเปิดแกลเลอรี
            Button(
                modifier = Modifier.width(180.dp),
                onClick = {
                    galleryLauncher.launch("image/*")
                }
            ) {
                Text(text = "เปิดอัลบั้ม")
            }

            Spacer(modifier = Modifier.height(50.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    val sharedPreferences = SharedPreferencesManager(context)
                    val idUsers = sharedPreferences.idUsers
                    val fullName = sharedPreferences.fnameLname ?: "ไม่มีชื่อ"

                    Log.d("PostScreen", "idUsers: $idUsers, fnameLname: $fullName")
                    if (idUsers == -1 ) {
                        Toast.makeText(context, "ไม่พบข้อมูลผู้ใช้", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

//                    if (imageUri == null) {
//                        Toast.makeText(contextForToast, "กรุณาเลือกรูปก่อน", Toast.LENGTH_SHORT).show()
//                        return@Button
//                    }
                    if (post_detail.isNullOrEmpty()) {
                        Toast.makeText(contextForToast, "กรุณากรอกรายละเอียดโพสต์", Toast.LENGTH_SHORT).show()
                        return@Button
                    }


                    val kkurentApi = kkurentApi.create()

                    try {
                        val inputStream = contextForToast.contentResolver.openInputStream(imageUri!!)
                        if (inputStream == null) {
                            Log.e("PostScreen", "Failed to open input stream")
                            throw Exception("Failed to open input stream")
                        }

                        val imageFile = File.createTempFile("image", ".jpg", contextForToast.cacheDir)
                        val outputStream = FileOutputStream(imageFile)

                        inputStream.copyTo(outputStream)
                        inputStream.close()
                        outputStream.close()

                        Log.d("PostScreen", "Image file created at: ${imageFile.absolutePath}")

                        val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
                        val fnameLnameBody = fullName.toRequestBody("text/plain".toMediaTypeOrNull())

                        val requestdetail = post_detail.toRequestBody("text/plain".toMediaTypeOrNull())
                        val requestId = idPost.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                        val userIdBody = idUsers.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                        kkurentApi.uploadPost(requestdetail, imagePart,userIdBody)
                            .enqueue(object : Callback<Post> {
                                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                                    if (response.isSuccessful) {
                                        Log.d("PostScreen", "Post uploaded successfully")
                                        Toast.makeText(navController.context, "โปรดรออนุมัติจากแอดมิน!", Toast.LENGTH_SHORT).show()
                                        navController.navigate(Screen.Home.route)
                                    } else {
                                        Log.e("PostScreen", "Failed to post: ${response.errorBody()?.string()}")
                                        Toast.makeText(navController.context, "ไม่สามารถโพสต์ได้!", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<Post>, t: Throwable) {
                                    Log.e("PostScreen", "Upload failed", t)
                                    Toast.makeText(navController.context, "เกิดข้อผิดพลาด!", Toast.LENGTH_SHORT).show()
                                }
                            })
                    } catch (e: Exception) {
                        Log.e("PostScreen", "Error occurred", e)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800)
                )
            ) {
                Text(text = "สร้างโพสต์", color = Color.White)
            }



        }
        }


    }


