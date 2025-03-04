package com.example.kkurent

import android.content.Context
import android.os.Parcelable
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.delay
import kotlinx.parcelize.Parcelize
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Parcelize
data class NotificationCount(
    @Expose
    @SerializedName("count") val count: Int,
) : Parcelable {}

@Parcelize
data class Notification(
    @Expose
    @SerializedName("idNotifications") val idNotifications : Int,

    @Expose
    @SerializedName("title") val title : String,

    @Expose
    @SerializedName("message") val message : String,

    @Expose
    @SerializedName("idUsers") val idUsers : String,

    @Expose
    @SerializedName("is_read") val isRead : Int,

    @Expose
    @SerializedName("createAt") val createdAt : String
): Parcelable {}

@Composable
fun NotificationButton(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = SharedPreferencesManager(context)
    val userId = sharedPreferences.idUsers
    var unreadCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            getUnreadNotificationCount(userId, context) {
                unreadCount = it.count
            }
            delay(1000)
        }
    }

    Box(
        modifier = Modifier
            .padding(top = 10.dp)
    ) {
        IconButton(
            onClick = { navController.navigate(Screen.Notification.route) }
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color(0xFFFFA655) // 🟠 Orange color
            )
        }

        // 🔴 **Unread Count Badge (Only if greater than 0)**
        if (unreadCount > 0) {
            Box(
                modifier = Modifier
                    .size(20.dp) // 🔥 Smaller badge
                    .background(Color.Red, shape = CircleShape)
                    .align(Alignment.TopEnd) // 🔹 Positioned at the top-right of the icon
                    .offset(y = (-2).dp), // 🔥 Adjusted for better placement
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = unreadCount.coerceAtMost(99).toString(),
                    fontSize = 14.sp, // 🔹 Smaller text
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }


}

fun getUnreadNotificationCount(userId: Int, context: Context, callback: (NotificationCount) -> Unit) {
    val createClient = kkurentApi.create()

    createClient.getUnreadNotificationCount(userId).enqueue(object : Callback<NotificationCount> {
        override fun onResponse(call: Call<NotificationCount>, response: Response<NotificationCount>) {
            if (response.isSuccessful) {
                response.body()?.let { callback(it) }
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        override fun onFailure(call: Call<NotificationCount>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}