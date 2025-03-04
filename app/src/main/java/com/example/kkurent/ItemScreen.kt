package com.example.kkurent

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream



@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(navController: NavHostController) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var idItems by remember { mutableIntStateOf(0) }
    var item_name = remember { mutableStateOf("") }
    var item_detail = remember { mutableStateOf("") }
    var item_img by remember { mutableStateOf<Uri?>(null) }
    var price by remember { mutableIntStateOf(0) }
    var selectedCategory = rememberSaveable { mutableStateOf("ของใช้ทั่วไป") }
    var selectedLocation = rememberSaveable { mutableStateOf("กังสดาล") }
    val createClient = kkurentApi.create()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        item_img = uri
        if (uri == null) {
            Toast.makeText(context, "คุณไม่ได้เลือกรูปภาพ", Toast.LENGTH_SHORT).show()
        }else {
            Log.d("DEBUG", "Selected Image: $uri")
        }
    }

    Column {
        CenterAlignedTopAppBar(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart =  16.dp, bottomEnd = 16.dp))
                .fillMaxWidth()
                .height(105.dp),
            title = {
                Text(text = "เพิ่มสินค้าใช้เช่า", color = Color.White, modifier = Modifier.offset(y = 10.dp))
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
                    value = item_name.value,
                    onValueChange = { item_name.value = it },
                    label = { Text(text = "ชื่อสินค้า") },
                )
            }
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .padding(5.dp),
                    value = item_detail.value,
                    onValueChange = { item_detail.value = it },
                    label = { Text(text = "คำอธิบายเพิ่มเติม") },
                    maxLines = 4
                )
            }
//            Spacer(modifier = Modifier.height(5.dp))
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(30.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = if (item_img != null) {
                                rememberAsyncImagePainter(item_img)
                            } else {
                                painterResource(id = R.drawable.image_search)
                            },
                            contentDescription = null,
                            modifier = Modifier.size(200.dp),
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

                    LocationDropdown(selectedLocation)
                    CategoryDropdown(selectedCategory)

                    Spacer(modifier = Modifier.height(25.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onClick@{
                            if (item_name.value.isBlank() || item_detail.value.isBlank() || price == 0) {
                                Toast.makeText(
                                    context,
                                    "กรุณากรอกข้อมูลให้ครบถ้วน!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@onClick
                            }

                            val sharedPreferences = SharedPreferencesManager(context)
                            val idUsers = sharedPreferences.idUsers
                            val fullName = sharedPreferences.fnameLname ?: "ไม่มีชื่อ"
                            item_img?.let { uri ->
                                val inputStream =
                                    context.contentResolver.openInputStream(uri) ?: return@let
                                val imageFile =
                                    File.createTempFile("image", ".jpg", context.cacheDir)

                                val outputStream = FileOutputStream(imageFile)
                                inputStream.copyTo(outputStream)
                                inputStream.close()
                                outputStream.close()

                                // แปลงไฟล์เป็น RequestBody
                                val requestBody =
                                    imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                                val imagePart = MultipartBody.Part.createFormData(
                                    "image",
                                    imageFile.name,
                                    requestBody
                                )

                                val idItemsBody = idItems.toString()
                                    .toRequestBody("text/plain".toMediaTypeOrNull())
                                val itemNameBody =
                                    item_name.value.toRequestBody("text/plain".toMediaTypeOrNull())
                                val itemDetailBody =
                                    item_detail.value.toRequestBody("text/plain".toMediaTypeOrNull())
                                val priceBody =
                                    price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                                val locationBody =
                                    selectedLocation.value.toRequestBody("text/plain".toMediaTypeOrNull())
                                val userIdBody = idUsers.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                                val itemStatusBody =
                                    "ว่าง".toRequestBody("text/plain".toMediaTypeOrNull()) // หรือดึงค่าจาก UI
                                val categoryBody =
                                    selectedCategory.value.toRequestBody("text/plain".toMediaTypeOrNull())
                                createClient.insertItems(
                                    imagePart,
                                    idItemsBody,
                                    itemNameBody,
                                    itemDetailBody,
                                    priceBody,
                                    locationBody,
                                    userIdBody,
                                    categoryBody,
                                    itemStatusBody

                                )
                                    .enqueue(object : Callback<Item> {
                                        override fun onResponse(
                                            call: Call<Item>,
                                            response: Response<Item>
                                        ) {
                                            if (response.isSuccessful) {
                                                Toast.makeText(
                                                    navController.context,
                                                    "โพสต์เรียบร้อยแล้ว!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navController.currentBackStackEntry?.savedStateHandle?.set<Int>(
                                                    "selectedScreen",
                                                    1
                                                )
                                                navController.navigate(Screen.Products.route)
                                            } else {
                                                Toast.makeText(
                                                    navController.context,
                                                    "ไม่สามารถโพสต์ได้! ${
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
                            } ?: Toast.makeText(
                                navController.context,
                                "กรุณาเลือกรูปภาพ",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                    ) {
                        Text(text = "เพิ่มสินค้า", color = Color.White)
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDropdown(selectedLocation: MutableState<String>) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val locationList = listOf("กังสดาล", "หลังมอ", "โคลัมโบ")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = Modifier.clickable { keyboardController?.hide() },
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier.width(340.dp).menuAnchor(),
            readOnly = true,
            value = selectedLocation.value,
            onValueChange = {},
            label = { Text("สถานที่") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            locationList.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedLocation.value = selectionOption
                        expanded = false
                    }
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(selectedCategory: MutableState<String>) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val categoryList = listOf(
        "ของใช้ทั่วไป",
        "เครื่องแต่งกาย",
        "อุปกรณ์อิเล็กทรอนิกส์",
        "หนังสือ",
        "กีฬา"
    )
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = Modifier.clickable { keyboardController?.hide() },
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier.width(340.dp).menuAnchor(),
            readOnly = true,
            value = selectedCategory.value, // ใช้ค่า selectedCategory จาก State
            onValueChange = {},
            label = { Text("หมวดหมู่") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categoryList.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedCategory.value = selectionOption // อัปเดตค่า
                        expanded = false
                    }
                )
            }
        }
    }
}



fun uriToMultipart(uri: Uri?, context: Context): MultipartBody.Part? {
    if (uri == null) return null
    val file = File(uri.path ?: return null)
    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
    return MultipartBody.Part.createFormData("file", file.name, requestFile)
}





