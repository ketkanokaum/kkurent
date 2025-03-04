package com.example.kkurent

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.kkurent.data.Chat
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBarChat(
    navController: NavController

) {
    Column {
        CenterAlignedTopAppBar(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart =  16.dp, bottomEnd = 16.dp))
                .fillMaxWidth()
                .height(105.dp),
            title = {},
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF0D346D), // สีน้ำเงินเข้ม
            ),
            navigationIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.logo3),
                    contentDescription = "Cart",
                    tint = Color(0xFFFFA655),
                    modifier = Modifier
                        .size(55.dp)
                        .padding(start = 15.dp, top = 1.dp)
                )
            },
            actions = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ข้อความ  | ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFA655), // สีส้ม
                        modifier = Modifier.padding(top = 5.dp)
                    )
                    NotificationButton(navController)
                }
            }
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {
    val context = LocalContext.current
    val currentUserId = SharedPreferencesManager(context).idUsers

    var chats by remember { mutableStateOf<List<Chat>>(emptyList()) }

    LaunchedEffect(Unit) {
        while (true) {
            getChats(currentUserId, context) { fetchedChats -> chats = fetchedChats }
            delay(1000) // Refresh chats every second
        }
    }

    // Background color for the entire screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MyTopAppBarChat(navController)

        if (chats.isEmpty()) {
            EmptyChatState()
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // ข้อความ "แชททั้งหมด"
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 135.dp) // ให้ข้อความชิดซ้ายและมีระยะห่างจากด้านบน
                        .fillMaxWidth() // กำหนดให้ข้อความเติมเต็มแนวนอน
                        .padding(start = 10.dp), // กำหนด padding ด้านซ้ายเล็กน้อย
                    text = "แชททั้งหมด (${chats.size})",
                    color = Color(0xFF0D346D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )


            Spacer(modifier = Modifier.height(20.dp))

                // เส้นแบ่งแนวนอน
                HorizontalDivider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                )

                // LazyColumn แสดงรายการแชท
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(chats) { chat ->
                        ChatItem(chat) {
                            navController.currentBackStackEntry?.savedStateHandle?.set<Int>(
                                "chatOtherUserId",
                                chat.otherUserId
                            )
                            navController.navigate(Screen.ChatDetail.route)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyChatState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .padding(16.dp)
    ) {
        Text(
            text = "ไม่มีแชท",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun ChatItem(chat: Chat, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for user avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray, shape = MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center
            ) {
                Text(text = chat.name.first().toString(), color = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = chat.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = timeAgoInThai(chat.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = chat.messageText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        // Bottom border
        HorizontalDivider(
            color = Color.LightGray,
            thickness = 1.dp,
        )
    }
}

fun timeAgoInThai(dateString: String): String {
    val inputInstant = Instant.parse(dateString) // แปลง string เป็น Instant
    val now = Instant.now() // เวลาปัจจุบัน
    val duration = Duration.between(inputInstant, now) // คำนวณระยะห่างของเวลา

    return when {
        duration.seconds < 60 -> "เมื่อสักครู่"
        duration.toMinutes() < 60 -> "${duration.toMinutes()} นาทีที่แล้ว"
        duration.toHours() < 24 -> "${duration.toHours()} ชั่วโมงที่แล้ว"
        duration.toDays() < 7 -> "${duration.toDays()} วันที่แล้ว"
        else -> "${duration.toDays() / 7} สัปดาห์ที่แล้ว"
    }
}

fun getChats(userId: Int, context: Context, callback: (List<Chat>) -> Unit) {
    kkurentApi.create().getChats(userId).enqueue(object : Callback<List<Chat>> {
        override fun onResponse(
            call: Call<List<Chat>>,
            response: Response<List<Chat>>
        ) {
            if (response.isSuccessful) {
                val items = response.body() ?: emptyList()
                callback(items)
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        override fun onFailure(call: Call<List<Chat>>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}