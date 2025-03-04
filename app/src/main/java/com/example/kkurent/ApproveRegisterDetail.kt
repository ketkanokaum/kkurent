package com.example.kkurent

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
//
//data class User(
//    val idUsers: Int = "001",
//    val fname_lname: String = "John Doe",
//    val email: String = "johndoe@example.com",
//    val address: String = "123 Main St, City, Country",
//    val bank: String = "ABC Bank",
//    val account_number: String = "1234567890",
//    val account_name: String = "John Doe",
//    val phone: String = "0912345678",
//    val profile_picture: String = "https://example.com/profile.jpg", // ตรวจสอบ URL
//    val id_card_image: String = "https://example.com/id_card.jpg" // ตรวจสอบ URL
//)
//
//val mockUser = User()

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ApproveRegisterDetail(navController: NavController) {
//    var showDialog by remember { mutableStateOf(false) }
//    var showFullImage by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("", color = Color.White) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) { //navController.navigate(Screen.RegistrationRequestScreen.route)
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_back),
//                            contentDescription = "Back",
//                            tint = Color.White,
//                            modifier = Modifier.size(24.dp)
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF0D346D))
//            )
//        }
//    ) { paddingValues ->
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(16.dp)
//        ) {
//            item {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .wrapContentHeight(), // ให้ขนาดพอดีกับเนื้อหาภายใน
//                    contentAlignment = Alignment.Center // จัดตำแหน่งให้ตรงกลาง
//                ) {
//                    AsyncImage(
//                        model = "https://i.pinimg.com/736x/a8/02/04/a8020483735b71de34997ca8ac0440ae.jpg",
//                        contentDescription = "Profile Picture",
//                        modifier = Modifier
//                            .size(200.dp)
//                            .clip(CircleShape)
//                            .border(2.dp, Color.Gray, CircleShape)
//                    )
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//            // ข้อมูลส่วนตัว (แยกเป็นการ์ด)
//            item {
//                Card(
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_person), // แทนที่ด้วยไอคอนที่คุณต้องการ
//                                contentDescription = "Name Icon",
//                                modifier = Modifier.size(20.dp),
//                                tint = Color.Unspecified
//                            )
//                            Spacer(modifier = Modifier.width(15.dp)) // เพิ่มช่องว่างระหว่างไอคอนและข้อความ
//                            Text("ชื่อ: ${mockUser.fname_lname}", style = MaterialTheme.typography.bodyLarge)
//                        }
//                    }
//                }
//            }
//
//            // อีเมล (แยกเป็นการ์ด)
//            item {
//                Card(
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_mail), // แทนที่ด้วยไอคอนที่คุณต้องการ
//                                contentDescription = "Name Icon",
//                                modifier = Modifier.size(20.dp),
//                                tint = Color.Unspecified
//                            )
//                            Spacer(modifier = Modifier.width(15.dp)) // เพิ่มช่องว่างระหว่างไอคอนและข้อความ
//                            Text("อีเมล: ${mockUser.email}", style = MaterialTheme.typography.bodyMedium)
//                        }
//                    }
//                }
//            }
//
//            // ที่อยู่ (แยกเป็นการ์ด)
//            item {
//                Card(
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_address), // แทนที่ด้วยไอคอนที่คุณต้องการ
//                                contentDescription = "Name Icon",
//                                modifier = Modifier.size(20.dp),
//                                tint = Color.Unspecified
//                            )
//                            Spacer(modifier = Modifier.width(15.dp)) // เพิ่มช่องว่างระหว่างไอคอนและข้อความ
//                            Text("ที่อยู่: ${mockUser.address}", style = MaterialTheme.typography.bodyMedium)
//                        }
//                    }
//                }
//            }
//
//            // เบอร์โทรศัพท์ (แยกเป็นการ์ด)
//            item {
//                Card(
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_phone), // แทนที่ด้วยไอคอนที่คุณต้องการ
//                                contentDescription = "Name Icon",
//                                modifier = Modifier.size(20.dp),
//                                tint = Color.Unspecified
//                            )
//                            Spacer(modifier = Modifier.width(15.dp)) // เพิ่มช่องว่างระหว่างไอคอนและข้อความ
//                            Text("โทรศัพท์: ${mockUser.phone}", style = MaterialTheme.typography.bodyMedium)
//                        }
//                    }
//                }
//            }
//
//            //รูปบัตรประชาชน (สามารถกดดูรูปได้)
//            item {
//                Card (
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                ){
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(200.dp)
//                            .padding(8.dp)
//                            .clickable { showFullImage = true }
//                    ) {
//                        AsyncImage(
//                            model = "https://i.pinimg.com/736x/86/ac/26/86ac26d84fc5458fd56b3796f793085c.jpg",
//                            contentDescription = "ID Card",
//                            modifier = Modifier.fillMaxSize()
//                        )
//                        if (showFullImage) {
//                            // แสดงรูปขนาดเต็มใน Popup
//                            Dialog(onDismissRequest = { showFullImage = false }) {
//                                Surface(
//                                    modifier = Modifier.fillMaxSize(),
//                                    shape = RoundedCornerShape(16.dp),
//                                    color = Color.Black.copy(alpha = 0.8f)
//                                ) {
//                                    Box(
//                                        contentAlignment = Alignment.Center,
//                                        modifier = Modifier.fillMaxSize()
//                                    ) {
//                                        AsyncImage(
//                                            model = "https://i.pinimg.com/736x/86/ac/26/86ac26d84fc5458fd56b3796f793085c.jpg",
//                                            contentDescription = "Full Size ID Card",
//                                            modifier = Modifier.fillMaxWidth().padding(16.dp)
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            // ข้อมูลธนาคาร (แยกเป็นการ์ด)
//            item {
//                Card(
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_bank), // แทนที่ด้วยไอคอนที่คุณต้องการ
//                                contentDescription = "Name Icon",
//                                modifier = Modifier.size(20.dp),
//                                tint = Color.Unspecified
//                            )
//                            Spacer(modifier = Modifier.width(15.dp)) // เพิ่มช่องว่างระหว่างไอคอนและข้อความ
//                            Text("ธนาคาร: ${mockUser.bank}", style = MaterialTheme.typography.bodyMedium)
//                        }
//                    }
//                }
//            }
//
//            item {
//                Card(
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_bank_number), // แทนที่ด้วยไอคอนที่คุณต้องการ
//                                contentDescription = "Name Icon",
//                                modifier = Modifier.size(20.dp),
//                                tint = Color.Unspecified
//                            )
//                            Spacer(modifier = Modifier.width(15.dp)) // เพิ่มช่องว่างระหว่างไอคอนและข้อความ
//                            Text("เลขบัญชี: ${mockUser.account_number}", style = MaterialTheme.typography.bodyMedium)
//                        }
//                    }
//                }
//            }
//
//            item {
//                Card(
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_bank_name), // แทนที่ด้วยไอคอนที่คุณต้องการ
//                                contentDescription = "Name Icon",
//                                modifier = Modifier.size(20.dp),
//                                tint = Color.Unspecified
//                            )
//                            Spacer(modifier = Modifier.width(15.dp)) // เพิ่มช่องว่างระหว่างไอคอนและข้อความ
//                            Text("ชื่อบัญชี: ${mockUser.account_name}", style = MaterialTheme.typography.bodyMedium)
//                        }
//                    }
//                }
//            }
//
//            // ปุ่ม Approve และ Reject (จัดวางให้สวย)
//            item {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Button(
//                        onClick = { showDialog = true },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
//                        modifier = Modifier.weight(1f).padding(end = 8.dp)
//                    ) {
//                        Text("ไม่อนุมัติ", color = Color.White)
//                    }
//
//                    Button(
//                        onClick = { showDialog = true },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF39B54A)),
//                        modifier = Modifier.weight(1f).padding(start = 8.dp)
//                    ) {
//                        Text("อนุมัติ", color = Color.White)
//                    }
//                }
//            }
//        }
//    }
//
//    // Popup ยืนยันการอนุมัติหรือปฏิเสธ
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            title = { Text("ยืนยันการดำเนินการ") },
//            text = { Text("คุณแน่ใจหรือไม่ที่จะดำเนินการ?") },
//            confirmButton = {
//                TextButton(onClick = { showDialog = false }) { Text("ยืนยัน") }
//            },
//            dismissButton = {
//                TextButton(onClick = { showDialog = false }) { Text("ยกเลิก") }
//            }
//        )
//    }
//}