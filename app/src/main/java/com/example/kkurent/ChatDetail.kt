package com.example.kkurent

import android.content.Context
import android.widget.Toast
import androidx.collection.emptyObjectList
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kkurent.SharedPreferencesManager.Companion.KEY_ACCOUNT_NAME
import com.example.kkurent.SharedPreferencesManager.Companion.KEY_ACCOUNT_NUMBER
import com.example.kkurent.SharedPreferencesManager.Companion.KEY_ADDRESS
import com.example.kkurent.SharedPreferencesManager.Companion.KEY_BANK
import com.example.kkurent.data.Message
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(navController: NavController) {
    val chatOtherUserId =
        navController.previousBackStackEntry?.savedStateHandle?.get<Int>("chatOtherUserId") ?: 0
    val context = LocalContext.current
    val sharedPreferences = SharedPreferencesManager(context)
    val currentUserId = sharedPreferences.idUsers

    var chatSendInfo by remember { mutableStateOf<ChatSendInfo>(ChatSendInfo(address = "", bank_account_info = "")) }
    
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var messageText by remember { mutableStateOf(TextFieldValue()) }
    var chatName by remember { mutableStateOf("") }
    var chatImage by remember { mutableStateOf("") }

    // Create LazyListState
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        getChatSendInfo(currentUserId, context) {
            fetchedChatSendInfo -> chatSendInfo = fetchedChatSendInfo
        }
        getChatHeader(chatOtherUserId, context) { fetchedChatHeader ->
            chatName = fetchedChatHeader.fname_lname
            chatImage = fetchedChatHeader.profile_picture
        }
        while (true) {
            getMessages(currentUserId, chatOtherUserId, context) { fetchedMessages ->
                messages = fetchedMessages
            }
            delay(1000) // Refresh messages every second
        }
    }

    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1) // scroll ไปยัง message item ตัวสุดท้าย
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ChatHeader(chatName, navController)
        HorizontalDivider(
            color = Color.LightGray,
            thickness = 1.dp,
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(messages) { message ->
                    MessageItem(message, currentUserId)
                }
            }
        }
        HorizontalDivider()
        MessageInputField(
            messageText = messageText,
            onMessageTextChange = { messageText = it },
            onSendClick = {
                sendMessage(currentUserId, chatOtherUserId, messageText.text.trim(), context) {
                    messageText = TextFieldValue("")
                    getMessages(currentUserId, chatOtherUserId, context) { fetchedMessages ->
                        messages = fetchedMessages
                    }
                }
            },
            onSendAddressClick = {
                sendMessage(currentUserId, chatOtherUserId, chatSendInfo.address, context) {
                    getMessages(currentUserId, chatOtherUserId, context) { fetchedMessages ->
                        messages = fetchedMessages
                    }
                }
            },
            onSendBankAccountInfoClick = {
                sendMessage(currentUserId, chatOtherUserId, chatSendInfo.bank_account_info, context) {
                    messageText = TextFieldValue("")
                    getMessages(currentUserId, chatOtherUserId, context) { fetchedMessages ->
                        messages = fetchedMessages
                    }
                }
            }
        )
    }
}

@Composable
fun MessageItem(message: Message, currentUserId: Int) {
    val isCurrentUser = message.senderId == currentUserId

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isCurrentUser) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray, shape = MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center
            ) {
                Text(text = message.senderFnameLname.first().toString(), color = Color.White)
            }
            Spacer(Modifier.width(16.dp))
        }
        Column(
            modifier = Modifier
                .background(
                    if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
        ) {
            Text(
                text = message.messageText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

@Composable
fun ChatHeader(chatName: String, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.End,
                text = chatName, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray, shape = MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center
            ) {
                Text(text = chatName.firstOrNull()?.toString() ?: "", color = Color.White)
            }
        }
    }
}

@Composable
fun MessageInputField(
    messageText: TextFieldValue,
    onMessageTextChange: (TextFieldValue) -> Unit,
    onSendClick: () -> Unit,
    onSendAddressClick: () -> Unit,
    onSendBankAccountInfoClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            // IconButton to trigger the dropdown
            IconButton(onClick = { expanded = true }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
            }

            // Dropdown Menu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("ส่งที่อยู่") },
                    onClick = {
                        onSendAddressClick()
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("ส่งข้อมูลบัญชีธนาคาร") },
                    onClick = {
                        onSendBankAccountInfoClick()
                        expanded = false
                    }
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            maxLines = 3,
            value = messageText,
            modifier = Modifier.weight(1f),
            onValueChange = onMessageTextChange,
            placeholder = { Text("พิมพ์ข้อความ...") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = onSendClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send Message")
        }
    }
}

data class ChatHeader(val fname_lname: String, val profile_picture: String)

data class ChatSendInfo(val address: String, val bank_account_info: String)

fun sendMessage(
    senderId: Int,
    receiverId: Int,
    messageText: String,
    context: Context,
    callback: () -> Unit
) {
    if (messageText.isBlank()) return

    kkurentApi.create().sendMessage(senderId, receiverId, messageText)
        .enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                if (response.isSuccessful) {
                    callback()
                } else {
                    showToast(context, "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                showToast(context, "Error: ${t.message}")
            }
        })
}

fun getChatSendInfo(
    userId: Int,
    context: Context,
    callback: (ChatSendInfo) -> Unit
) {
    kkurentApi.create().getChatSentInfo(userId).enqueue(object : Callback<ChatSendInfo> {
        override fun onResponse(call: Call<ChatSendInfo>, response: Response<ChatSendInfo>) {
            if (response.isSuccessful) {
                response.body()?.let { callback(it) }
            } else {
                showToast(context, "Error: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<ChatSendInfo>, t: Throwable) {
            showToast(context, "Error: ${t.message}")
        }
    })
}

fun getChatHeader(
    userId: Int,
    context: Context,
    callback: (ChatHeader) -> Unit
) {
    kkurentApi.create().getChatheader(userId).enqueue(object : Callback<ChatHeader> {
        override fun onResponse(call: Call<ChatHeader>, response: Response<ChatHeader>) {
            if (response.isSuccessful) {
                response.body()?.let { callback(it) }
            } else {
                showToast(context, "Error: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<ChatHeader>, t: Throwable) {
            showToast(context, "Error: ${t.message}")
        }
    })
}


fun getMessages(
    userId: Int,
    otherUserId: Int,
    context: Context,
    callback: (List<Message>) -> Unit
) {
    kkurentApi.create().getMessages(userId, otherUserId).enqueue(object : Callback<List<Message>> {
        override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
            if (response.isSuccessful) {
                callback(response.body() ?: emptyList())
            } else {
                showToast(context, "Error: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<List<Message>>, t: Throwable) {
            showToast(context, "Error: ${t.message}")
        }
    })
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
