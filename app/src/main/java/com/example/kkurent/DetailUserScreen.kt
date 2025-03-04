package com.example.kkurent

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailUserScreen(navController: NavHostController) {
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Register>("data")
    var id by remember { mutableStateOf(data?.idUsers ?: 0) }
    var fname_lname by remember { mutableStateOf(data?.fname_lname ?: "") }
    var email by remember { mutableStateOf(data?.email ?: "") }
    var address by remember { mutableStateOf(data?.address ?: "") }
    var bank by remember { mutableStateOf(data?.bank ?: "") }
    var account_number by remember { mutableStateOf(data?.account_number ?: "ว่าง") }
    var account_name by remember { mutableStateOf(data?.account_name ?: "ว่าง") }
    var phone by remember { mutableStateOf(data?.phone ?: "") }
    var profile_picture by remember { mutableStateOf(data?.profile_picture ?: "") }
    var id_card_img by remember { mutableStateOf(data?.id_card_img ?: "") }
    var role by remember { mutableStateOf(data?.role ?: "") }
    val createClient = userApi.create()

    var datauser by remember { mutableStateOf<Register?>(null) }
    val context = LocalContext.current
    var softDeleteDialog by remember {  mutableStateOf(false) }

    var showDialogProfile by remember { mutableStateOf(false) }
    var showDialogIdCard by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        datauser = data
    }

    if (softDeleteDialog) {
        AlertDialog(
            onDismissRequest = { softDeleteDialog = false },
//            title = { Text("Warning") },
            text = { Text("คุณต้องการลบบัญชีผู้ใช้นี้ใช่หรือไม่") },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (id == 0) {
                            Toast.makeText(context, "ไม่พบ ID Post", Toast.LENGTH_SHORT).show()
                            return@TextButton
                        }

                        createClient.delete_softUsers(id).enqueue(object : Callback<Register> {
                            override fun onResponse(call: Call<Register>, response: Response<Register>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "ลบบัญชีผู้ใช้สำเร็จ!", Toast.LENGTH_SHORT).show()
                                    datauser = null

                                    softDeleteDialog = false
                                    navController.popBackStack(Screen.DetailUser.route, false)
                                    navController.navigate(Screen.ShowAllUsers.route)
                                } else {
                                    Toast.makeText(context, "ลบผู้ใช้สำเร็จไม่สำเร็จ!", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Register>, t: Throwable) {
                                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                ) {
                    Text("ใช่")
                }
            },
            dismissButton = {
                TextButton(onClick = { softDeleteDialog = false }) {
                    Text("ไม่")
                }
            }
        )
    }


    // Dialog แสดงรูปโปร์ไฟล์
    if (showDialogProfile) {
        Dialog(onDismissRequest = { showDialogProfile = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { showDialogProfile = false },
                contentAlignment = Alignment.Center
            ) {
                Spacer(modifier = Modifier.height(50.dp)) // เว้นระยะห่างด้านบน
                Text(
                    text = "รูปโปรไฟล์",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 7.dp)
                )
                Spacer(modifier = Modifier.height(50.dp)) // เว้นระยะห่างด้านบน
                Image(
                    painter = if (profile_picture != "no image") {
                        rememberAsyncImagePainter(profile_picture)
                    } else {
                        rememberAsyncImagePainter(R.drawable.logo3)
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .size(300.dp) // กำหนดขนาดใหญ่ขึ้น
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                )
            }
        }
    }

    // Dialog แสดงรูปบัตรประชาชน
    if (showDialogIdCard) {
        Dialog(onDismissRequest = { showDialogIdCard = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { showDialogIdCard = false },
                contentAlignment = Alignment.Center
            ) {
                Spacer(modifier = Modifier.height(50.dp)) // เว้นระยะห่างด้านบน
                Text(
                    text = "รูปบัตรประชาชน",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 7.dp)
                )
                Spacer(modifier = Modifier.height(50.dp)) // เว้นระยะห่างด้านบน
                Image(
                    painter = if (id_card_img != "no image") {
                        rememberAsyncImagePainter(id_card_img)
                    } else {
                        rememberAsyncImagePainter(R.drawable.logo3)
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .size(300.dp) // กำหนดขนาดใหญ่ขึ้น
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                )
            }
        }
    }

    // แสดงรายละเอียดสินค้า
    datauser?.let { currentItem ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(105.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(Color(0xFF003366))
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "ข้อมูลผู้ใช้งาน", color = Color.White)
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
                        containerColor = Color.Transparent
                    )
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                .padding(top = 115.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 12.dp, end = 12.dp),
                        elevation = CardDefaults.elevatedCardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F1F9))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // รูปโปรไฟล์
                            Spacer(modifier = Modifier.height(16.dp)) // เว้นระยะห่างด้านบน
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                                    .clickable { showDialogProfile = true }, // คลิกเพื่อเปิด Dialog
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = if (profile_picture != "no image") {
                                        rememberAsyncImagePainter(profile_picture)
                                    } else {
                                        rememberAsyncImagePainter(R.drawable.logo3)
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(100.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp)) // เว้นระยะห่างด้านล่าง

                            // แสดงชื่อผู้ใช้ ชิดซ้าย - ขวา
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "ชื่อนามสกุล",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 7.dp)
                                )
                                Text(
                                    text = fname_lname,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(end = 7.dp)
                                )
                            }

                            // อีเมล
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "อีเมล",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 7.dp)
                                )
                                Text(
                                    text = email,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(end = 7.dp)
                                )
                            }

                            // ที่อยู่
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "ที่อยู่",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 7.dp)
                                )
                                Text(
                                    text = address,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 60.dp, end = 7.dp)
                                )
                            }

                            // รูปประชาชน
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)  .clickable { showDialogIdCard = true }, // คลิกเพื่อเปิด Dialog,
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = if (id_card_img != "no image") {
                                        rememberAsyncImagePainter(id_card_img)
                                    } else {
                                        rememberAsyncImagePainter(R.drawable.logo3)
                                    },
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(270.dp)
                                        .align(Alignment.Center)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // ข้อมูลธนาคาร
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "ธนาคาร",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 7.dp)
                                )
                                Text(
                                    text = bank,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(end = 7.dp)
                                )
                            }

                            // ชื่อบัญชี
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "ชื่อบัญชี",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 7.dp)
                                )
                                Text(
                                    text = account_name,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(end = 7.dp)
                                )
                            }

                            // หมายเลขบัญชี
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "หมายเลขบัญชี",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 7.dp)
                                )
                                Text(
                                    text = account_number,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(end = 7.dp)
                                )
                            }

                            // เบอร์โทร
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "เบอร์โทร",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 7.dp)
                                )
                                Text(
                                    text = phone,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(end = 7.dp)
                                )
                            }
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(1f),
                            onClick = {  if (id != null && id != 0) {
                                id = id
                                softDeleteDialog = true
                            } else {
                                Toast.makeText(context, "Invalid User ID", Toast.LENGTH_SHORT).show()

                            }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text(text = "ลบผู้ใช้", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
