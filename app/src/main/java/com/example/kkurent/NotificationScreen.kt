package com.example.kkurent

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {
    val context = LocalContext.current
    var notifications by remember { mutableStateOf<List<Notification>>(emptyList()) }
    val sharedPreferencesManager = SharedPreferencesManager(context)
    val userId = sharedPreferencesManager.idUsers

    var filterOption by rememberSaveable { mutableStateOf("ทั้งหมด") }

    // 🔹 แสดงจำนวนแจ้งเตือน
    val filteredNotifications = when (filterOption) {
        "ยังไม่อ่าน" -> notifications.filter { it.isRead == 0 }
        else -> notifications
    }

    // ✅ ใช้ LaunchedEffect ดึงข้อมูลใหม่ทุก 1 วินาที
    LaunchedEffect(Unit) {
        while (true) {
            getNotifications(userId, context) { fetchedNotifications ->
                notifications = fetchedNotifications
            }
            delay(1000) // ✅ Auto-refresh ทุก 1 วินาที
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "การแจ้งเตือน", color = Color.White, fontSize = 20.sp) },
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // ✅ ป้องกันซ้อนทับกับ Top Bar
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 🔹 แท็บกรอง (Segmented Control)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFE0E0E0)), // ✅ พื้นหลังสีเทาอ่อน
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { filterOption = "ทั้งหมด" },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = if ("ทั้งหมด" == filterOption) Color(0xFF003366) else Color.Transparent,
                        contentColor = if ("ทั้งหมด" == filterOption) Color.White else Color.Black
                    ),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f)
                ) {
                    Text("ทั้งหมด", fontWeight = FontWeight.Bold)
                }
                TextButton(
                    onClick = { filterOption = "ยังไม่อ่าน" },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = if ("ยังไม่อ่าน" == filterOption) Color(0xFF003366) else Color.Transparent,
                        contentColor = if ("ยังไม่อ่าน" == filterOption) Color.White else Color.Black
                    ),
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                ) {
                    Text("ยังไม่อ่าน", fontWeight = FontWeight.Bold)
                }
            }

            // ✅ ทำให้ LazyColumn สามารถสกรอลล์ได้
            LazyColumn() {
                if (filteredNotifications.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            text = if (filterOption == "ทั้งหมด") "❌ ไม่มีแจ้งเตือน" else "❌ ไม่มีแจ้งเตือนที่ยังไม่อ่าน",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                        )
                    }
                } else {
                    items(filteredNotifications) { notification ->
                        NotificationItem(notification) {
                            readNotification(notification.idNotifications, context) {
                                getNotifications(userId, context) { fetchedNotifications ->
                                    notifications = fetchedNotifications
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 🔹 UI ของ Notification Item
@Composable
fun NotificationItem(notification: Notification, onClick: () -> Unit) {
    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        .withZone(ZoneId.systemDefault())

    val formattedDate = outputFormatter.format(Instant.parse(notification.createdAt))

    val backgroundColor = if (notification.isRead == 1) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) // ✅ เบามากสำหรับ "อ่านแล้ว"
    } else {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) // ✅ เน้นนิดๆ สำหรับ "ยังไม่อ่าน"
    }

    val titleColor = if (notification.isRead == 1) {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // ✅ สีอ่อนลง
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )

                if (notification.isRead == 1) {
                    Text(
                        text = "✔ อ่านแล้ว!",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.message,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = if (notification.isRead == 1) 0.6f else 1f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formattedDate,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (notification.isRead == 1) 0.6f else 1f)
            )
        }
    }
}

fun readNotification(notificationId: Int, context: Context, callback: () -> Unit) {
    val createClient = kkurentApi.create()

    createClient.readNotification(notificationId).enqueue(object : Callback<Notification> {
        override fun onResponse(call: Call<Notification>, response: Response<Notification>) {
            if (response.isSuccessful) {
                callback()
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        override fun onFailure(call: Call<Notification>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

fun getNotifications(userId: Int, context: Context, callback: (List<Notification>) -> Unit) {
    val createClient = kkurentApi.create()

    createClient.getNotifications(userId).enqueue(object : Callback<List<Notification>> {
        override fun onResponse(
            call: Call<List<Notification>>,
            response: Response<List<Notification>>
        ) {
            if (response.isSuccessful) {
                val items = response.body() ?: emptyList()
                callback(items)
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}
