package com.example.kkurent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(navController: NavHostController) {
    val context = LocalContext.current

    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Item>("data") ?: Item(0,"","",0,"","","","","",0)

    var id by remember { mutableStateOf(data?.idItems ?: 0) }
    var item_name by remember { mutableStateOf(data?.item_name ?: "") }
    var item_detail by remember { mutableStateOf(data?.item_detail ?: "") }
    var item_img by remember { mutableStateOf(data?.item_img ?: "") }
    var item_status by remember { mutableStateOf(data?.item_status ?: "") }
    var Location = rememberSaveable { mutableStateOf(data?.location ?: "") }
    var Category = rememberSaveable { mutableStateOf(data?.category_name ?: "") }
    var price by remember { mutableStateOf(data?.price ?: 0) }
    val contextForToast = LocalContext.current.applicationContext
    val createClient = kkurentApi.create()


    var newImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher สำหรับเลือกภาพจากแกลเลอรี
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        newImageUri = uri
        if (uri == null) {
            Toast.makeText(context, "คุณไม่ได้เลือกรูปภาพ", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("DEBUG", "Selected Image: $uri")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .fillMaxWidth()
            .height(100.dp)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(text = "แก้ไขโพสต์ของคุณ", color = Color.White)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    value = item_name,
                    onValueChange = { item_name = it },
                    label = { Text(text = "ชื่อสินค้า") },
                )
            }
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .padding(5.dp),
                    value = item_detail,
                    onValueChange = { item_detail = it },
                    label = { Text(text = "คำอธิบายเพิ่มเติม") },
                    maxLines = 4
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),

                    ) {

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(30.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        val painter = when {
                            newImageUri != null -> rememberAsyncImagePainter(newImageUri)
                            item_img.isNotBlank() -> rememberAsyncImagePainter(item_img)
                            else -> painterResource(id = R.drawable.image_search)
                        }
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        modifier = Modifier.width(180.dp),
                        onClick = {
                            galleryLauncher.launch("image/*")
                        }
                    ) {
                        Text(text = "เปิดอัลบั้ม")
                    }

                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "รายละเอียดสินค้า",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        value = if (price == 0) "" else price.toString(),
                        onValueChange = {
                            price = it.toIntOrNull() ?: 0
                        },
                        label = { Text(text = "ราคา") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions.Default,
                        shape = RoundedCornerShape(12.dp)
                    )

                    LocationDropdown(Location)
                    CategoryDropdown(Category)


                    Row(
                        Modifier.fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Button(
                            modifier = Modifier.width(130.dp),
                            onClick = {


                                val imagePart: MultipartBody.Part? = if (newImageUri != null) {
                                    // ✅ ถ้ามีการเลือกภาพใหม่ ให้อัปโหลดไฟล์
                                    val inputStream = contextForToast.contentResolver.openInputStream(
                                        newImageUri!!
                                    )
                                        ?: throw Exception("Failed to open input stream")

                                    val imageFile = File.createTempFile("image", ".jpg", contextForToast.cacheDir)
                                    val outputStream = FileOutputStream(imageFile)

                                    inputStream.copyTo(outputStream)
                                    inputStream.close()
                                    outputStream.close()

                                    val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                                    MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
                                } else {
                                    null // ✅ ถ้าไม่ได้เลือกภาพใหม่ ใช้รูปเดิมจากฐานข้อมูล
                                }


                                    // สร้าง RequestBody สำหรับข้อมูลอื่น ๆ

                                    val itemNameBody =
                                        item_name.toRequestBody("text/plain".toMediaTypeOrNull())
                                    val itemDetailBody =
                                        item_detail.toRequestBody("text/plain".toMediaTypeOrNull())
                                    val priceBody = price.toString()
                                        .toRequestBody("text/plain".toMediaTypeOrNull())
                                    val locationBody =
                                        Location.value.toRequestBody("text/plain".toMediaTypeOrNull())
                                    val categoryBody =
                                        Category.value.toRequestBody("text/plain".toMediaTypeOrNull())
                                    val itemStatusBody =
                                        item_status.toRequestBody("text/plain".toMediaTypeOrNull())

                                    Log.d("DEBUG", "idItems: $id")

                                    // เรียก API เพื่ออัปเดตข้อมูลสินค้า
                                    createClient.editItems(
                                        id,
                                        imagePart,
                                        itemNameBody,
                                        itemDetailBody,
                                        priceBody,
                                        locationBody,
                                        categoryBody,
                                        itemStatusBody,

                                    ).enqueue(object : Callback<Item> {
                                        override fun onResponse(
                                            call: Call<Item>,
                                            response: Response<Item>
                                        ) {
                                            if (response.isSuccessful) {
                                                Toast.makeText(
                                                    navController.context,
                                                    "แก้ไขเรียบร้อยแล้ว!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navController.navigate(Screen.Products.route)
                                            } else {
                                                Toast.makeText(
                                                    navController.context,
                                                    "ไม่สามารถแก้ไขได้! ${
                                                        response.errorBody()?.string()
                                                    }",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<Item>, t: Throwable) {
                                            Toast.makeText(
                                                navController.context,
                                                "เกิดข้อผิดพลาด! ${t.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })

                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                        ) {
                            Text(text = "แก้ไขสินค้า", color = Color.White)
                        }
                    }
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
