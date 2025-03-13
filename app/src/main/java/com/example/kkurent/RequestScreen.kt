package com.example.kkurent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestScreen(navController: NavController) {
    val showDialog = remember { mutableStateOf(false) }
    val showCancelDialog = remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "โพสต์ที่รออนุมัติ",
                fontSize = 25.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(IntrinsicSize.Min),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    // ข้อมูลเจ้าของโพสต์
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Ketkanok",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    // รายละเอียดสินค้า
                    Text("ชื่อสินค้า: เครื่องคิดเลขวิทยาศาสตร์", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    Text(
                        "รายละเอียดสินค้า: เครื่องคิดเลขวิทยาศาสตร์รุ่นใหม่ ใช้งานง่าย ฟังก์ชันครบถ้วน", fontSize = 9.sp)
                    Text("ราคาน่า: เช่ารับละ 10 บาท", fontSize = 9.sp)
                    Text("สถานที่: กังสดาล ขอนแก่น", fontSize = 9.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    // รูปสินค้า
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .shadow(10.dp, RoundedCornerShape(10.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.heart_filled),
                            contentDescription = "Product Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // ปุ่มอนุมัติ / ปฏิเสธ
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FloatingActionButton(
                            onClick = { showDialog.value = true },
                            containerColor = Color(0xFF4CAF50),
                            shape = CircleShape
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "confirm", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        FloatingActionButton(
                            onClick = { showCancelDialog.value = true },
                            containerColor = Color(0xFFFF0000),
                            shape = CircleShape
                        ) {
                            Icon(imageVector = Icons.Default.Cancel, contentDescription = "Cancel", tint = Color.White)
                        }
                    }
                }
            }
        }
    }

    // Dialog อนุมัติ
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text(
                    "คุณแน่ใจหรือว่า\nต้องการอนุมัติโพสต์นี้",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = { showDialog.value = false }) {
                        Text("ยกเลิก", color = Color.Gray)
                    }
                    TextButton(onClick = { showDialog.value = false }) {
                        Text("ตกลง", color = Color.Blue)
                    }
                }
            }
        )
    }


    // Dialog ปฏิเสธ
    if (showCancelDialog.value) {
        AlertDialog(
            onDismissRequest = { showCancelDialog.value = false },
            title = {
                Text(
                    "คุณแน่ใจหรือว่า\nต้องการปฏิเสธโพสต์นี้",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = { showCancelDialog.value = false }) {
                        Text("ยกเลิก", color = Color.Gray)
                    }
                    TextButton(onClick = { showCancelDialog.value = false }) {
                        Text("ตกลง", color = Color.Green)
                    }
                }
            }
        )
    }
}
