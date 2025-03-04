package com.example.kkurent

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import com.example.kkurent.Screen.Chat.name
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostScreen(navController: NavHostController) {

    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Post>("data") ?:Post(0,"","",0,"")
    var id by remember { mutableStateOf(data?.idPost ?: 0) }
    var post_detail by remember { mutableStateOf(data?.post_detail ?: "") }
    var post_img by remember { mutableStateOf(data?.post_img ?: "") }
    val contextForToast = LocalContext.current.applicationContext
    val createClient = kkurentApi.create()
    var post by remember { mutableStateOf<Post?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> imageUri = uri }
    )
    val idRequestBody = id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    val imageUrl = imageUri?.toString() ?: "$post_img?t=${System.currentTimeMillis()}"






//    val galleryLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent(),
//        onResult = { uri ->
//            uri?.let {
//                imageUri = it
//            }
//        }
//
//    )
    Column{
        CenterAlignedTopAppBar(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart =  16.dp, bottomEnd = 16.dp))
                .fillMaxWidth()
                .height(105.dp),
            title = {
                Text(
                    text = if (id == 0) "สร้างโพสของคุณ" else "แก้ไขโพสของคุณ",
                    color = Color.White)
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
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
                        rememberAsyncImagePainter(post_img)
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )

            }

            val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    imageUri = uri
                }
            }

            Button(onClick = { pickImageLauncher.launch("image/*") }) {
                Text("เลือกภาพ")
            }


            Row(
                Modifier.fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

//                Button(
//                    modifier = Modifier.width(130.dp),
//                    onClick = {
//                        imageUri?.let { uri ->
//                            val inputStream = contextForToast.contentResolver.openInputStream(uri) ?: return@let
//                            val imageFile = File.createTempFile("image", ".jpg")
//                            val outputStream = FileOutputStream(imageFile)
//                            inputStream.copyTo(outputStream)
//                            inputStream.close()
//                            outputStream.close()
//                            val requestBody=imageFile.asRequestBody("image/jpg".toMediaTypeOrNull())
//                            val imagePart = MultipartBody.Part.createFormData("image",imageFile.name,requestBody)
//                            val idRequestBody =id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
//                            val postdetailRequestBody = post_detail.toRequestBody("text/plain".toMediaTypeOrNull())
//
//
//                        createClient.edit_post(idRequestBody, postdetailRequestBody, imagePart).enqueue(object : Callback<Post> {
//                            override fun onResponse(call: Call<Post>, response: Response<Post>) {
//                                if (response.isSuccessful) {
//                                    Toast.makeText(contextForToast, "แก้ไขโพสต์สำเร็จ!", Toast.LENGTH_SHORT).show()
//                                    Log.d("Success", "แก้ไขโพสต์สำเร็จ")
//                                    navController.navigate(Screen.Home.route)
//                                } else {
//                                    Toast.makeText(contextForToast, "ไม่สามารถแก้ไขโพสต์ได้", Toast.LENGTH_SHORT).show()
//                                    Log.e("Error", "ไม่สามารถแก้ไขโพสต์ได้: ${response.errorBody()?.string()}")
//                                }
//                            }
//
//                            override fun onFailure(call: Call<Post>, t: Throwable) {
//                                Toast.makeText(contextForToast, "เกิดข้อผิดพลาด: ${t.message}", Toast.LENGTH_SHORT).show()
//                                Log.e("Failure", "Error: ${t.message}")
//                            }
//                        })
//                        } ?: run {
//                            Toast.makeText(contextForToast, "ไม่สามารถอัปโหลดรูปได้", Toast.LENGTH_SHORT).show()
//                        }
//
//                    }
//                ) {
//                    Text("แก้ไข")
//                }
                Button(
                    modifier = Modifier.width(130.dp),
                    onClick = {
                        val postdetailRequestBody = post_detail.toRequestBody("text/plain".toMediaTypeOrNull())

                        val imagePart: MultipartBody.Part? = imageUri?.let { uri ->
                            val inputStream = context.contentResolver.openInputStream(uri) ?: return@let null
                            val imageFile = File(context.cacheDir, "image.jpg")
                            val outputStream = FileOutputStream(imageFile)
                            inputStream.copyTo(outputStream)
                            inputStream.close()
                            outputStream.close()

                            val requestBody = imageFile.asRequestBody("image/jpg".toMediaTypeOrNull())
                            MultipartBody.Part.createFormData("post_img", imageFile.name, requestBody)
                        }

                        // 🟢 Log ข้อมูลที่ส่งไป API
                        Log.d("EditPost", "Sending request -> id: $id, post_detail: $post_detail, hasImage: ${imageUri != null}")
                        Log.d("EditPost", "🔹 Sending request to API")
                        Log.d("EditPost", "📌 idPost: $id")
                        Log.d("EditPost", "📌 post_detail: $post_detail")
                        Log.d("EditPost", "📌 Image Selected: ${imageUri != null}")

                        createClient.edit_post(id, postdetailRequestBody, imagePart)
                            .enqueue(object : Callback<Post> {
                                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(context, "แก้ไขโพสต์สำเร็จ!", Toast.LENGTH_SHORT).show()
                                        navController.navigate(Screen.Home.route)
                                    } else {
                                        val errorBody = response.errorBody()?.string()
                                        Log.e("EditPost", "Response Code: ${response.code()}, Error: $errorBody")
                                        Toast.makeText(context, "อัปเดตล้มเหลว: $errorBody", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<Post>, t: Throwable) {
                                    Log.e("EditPost", "Failure: ${t.message}")
                                    Toast.makeText(context, "เกิดข้อผิดพลาด: ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                ) {
                    Text("แก้ไข")
                }




                Spacer(modifier = Modifier.width(width = 10.dp))
                Button(
                    modifier = Modifier.width(130.dp),
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Text("ยกเลิก")
                }
            }


        }
    }
}
