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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.layout.ContentScale
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBarChat(navController: NavController) {
    Column {
        CenterAlignedTopAppBar(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
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
                        text = "แชท  | ",
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
                modifier = Modifier.fillMaxSize().padding(top = 105.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "แชททั้งหมด",
                            fontSize = 20.sp,
                            color = Color(0xFF0D346D),
                            fontWeight = FontWeight.Bold,
                        )
                        Icon(
                            Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "all chats",
                            tint = Color(0xFF0D346D),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "${chats.size} รายการ",
                        color = Color.Gray,
                        fontSize = 16.sp,
                    )
                }

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
            Image(
                painter = rememberAsyncImagePainter(chat.image),
                contentDescription = chat.messageText,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = formatDateTime(chat.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                    )
                }
                Text(
                    text = chat.messageText.replace("\n", " "),
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