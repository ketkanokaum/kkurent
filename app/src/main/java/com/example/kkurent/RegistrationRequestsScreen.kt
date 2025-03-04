package com.example.kkurent.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.kkurent.RegistrationRequest
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationRequestsScreen(navController: NavHostController) {
//    val viewModel: RegistrationViewModel = viewModel()
//    val requests by viewModel.requests.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.fetchRequests()
//    }
//
//    Scaffold(
//        topBar = { TopAppBar(title = { Text("คำขอลงทะเบียน") }) }
//    ) { padding ->
//        Box(modifier = Modifier.padding(padding)) {
//            LazyColumn {
//                items(requests) { request ->
//                    RequestItem(
//                        request = request,
//                        onApprove = { viewModel.updateRequestStatus(request.id, "Approved") },
//                        onReject = { viewModel.updateRequestStatus(request.id, "Rejected") },
//                        onDetailsClick = {
//                            navController.navigate("registrationDetail/${request.id}")
//                        }
//                    )
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun RequestItem(
//    request: RegistrationRequest,
//    onApprove: () -> Unit,
//    onReject: () -> Unit,
//    onDetailsClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = "ชื่อ: ${request.name}", style = MaterialTheme.typography.bodyLarge)
//            Text(text = "วันที่คำขอลงทะเบียน: ${request.date}", style = MaterialTheme.typography.bodyMedium)
//            Text(text = "ดูรายละเอียดเพิ่มเติม...", style = MaterialTheme.typography.bodyMedium, color = Color.Blue)
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Button(onClick = onApprove, colors = ButtonDefaults.buttonColors(containerColor = Color.Green)) {
//                    Text("อนุมัติ")
//                }
//                Button(onClick = onReject, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
//                    Text("ปฏิเสธ")
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun RegistrationDetailScreen(navController: NavHostController, requestId: String) {
//    val viewModel: RegistrationViewModel = viewModel()
//    // สังเกต LiveData หรือ State เพื่อดึงข้อมูล
//    val requests by viewModel.requests.collectAsState(initial = emptyList())
//
//    val request = requests.find { it.id.toString() == requestId }
//
//    if (request != null) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Text(text = "รายละเอียดคำขอ", fontSize = 24.sp, fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // รูปโปรไฟล์
//            Image(painter = rememberAsyncImagePainter(request.profileImage), contentDescription = null)
//
//            // ข้อมูลส่วนตัว
//            Text(text = "ชื่อ: ${request.name}", fontSize = 18.sp)
//            Text(text = "ที่อยู่: ${request.address}", fontSize = 18.sp)
//            Text(text = "ธนาคาร: ${request.bank}", fontSize = 18.sp)
//            Text(text = "เบอร์โทร: ${request.phone_number}", fontSize = 18.sp)
//
//            // รูปบัตรประจำตัวประชาชน
//            Image(painter = rememberAsyncImagePainter(request.id_card_img), contentDescription = null)
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // ปุ่มอนุมัติ/ไม่อนุมัติ
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                Button(
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
//                    onClick = { /* ปฏิเสธ */ }
//                ) {
//                    Text("ไม่อนุมัติ", color = Color.White)
//                }
//
//                Button(
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
//                    onClick = { /* อนุมัติ */ }
//                ) {
//                    Text("อนุมัติ", color = Color.White)
//                }
//            }
//        }
//    } else {
//        // หากไม่พบข้อมูลคำขอที่มี requestId นี้
//        Text(text = "ไม่พบข้อมูลคำขอ", fontSize = 18.sp)
//    }
}