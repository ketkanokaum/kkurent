package com.example.kkurent

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.kkurent.SharedPreferencesManager.Companion.KEY_FNAME_LNAME
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, idItems: String) {
    val itemId = idItems.toIntOrNull() ?: 0
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    val loggedInUserFnameLname = sharedPreferences.getString(KEY_FNAME_LNAME, null)
    var item by remember { mutableStateOf<Item?>(null) }
    val currentUserFnameLname = sharedPreferences.getString("key_fname_lname", "")

    // ดึงข้อมูลสินค้าเมื่อเปิดหน้า
    LaunchedEffect(Unit) {
        showItemDetails(itemId, context) { retrievedItem ->
            item = retrievedItem
        }
    }

    // แสดงรายละเอียดสินค้า
    item?.let { currentItem ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(Color(0xFF003366))
            ) {
                CenterAlignedTopAppBar(
                    title = {},
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

            // รูปภาพสินค้า
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (70).dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = rememberAsyncImagePainter(currentItem.item_img),
                    contentDescription = "Item Image",
                    modifier = Modifier
                        .requiredSize(300.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            }

            // ข้อมูลสินค้า
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (390).dp)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    if (currentItem.fname_lname == currentUserFnameLname) {
                        Text(
                            text = "โพสต์โดยคุณ",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    } else {
                        Text(
                            text = "โพสต์โดย: ${currentItem.fname_lname}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
                Text(
                    text = currentItem.item_name,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 16.dp, start = 7.dp)
                )
                Text(
                    text = currentItem.item_detail,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(bottom = 16.dp, start = 7.dp)
                )

                // ที่ตั้งสินค้า
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Icon",
                        tint = Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "สถานที่: ${currentItem.location}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // สถานะสินค้า
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Status Icon",
                        tint = Color.Blue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "สถานะ: ${currentItem.item_status}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "ราคา: ${currentItem.price} ต่อวัน",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003366),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 2.dp, end = 7.dp)
                )
            }

            // ปุ่มต่างๆ
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (600).dp)
                    .padding(top = 40.dp, end = 16.dp, start = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        // เพิ่มเข้ารายการโปรด
                        val sharedPreferences = SharedPreferencesManager(context)

                        // ถ้ายังไม่ login ไล่ไป login ก่อน
                        if (!sharedPreferences.isLoggedIn) {
                            Toast.makeText(context, "กรุณาเข้าสู่ระบบก่อน!", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            addFavorite(itemId, sharedPreferences.idUsers, context)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "รายการโปรด", color = Color.White)
                }
                if (!SharedPreferencesManager(context).isCurrentUser(currentItem.user_id)) {
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set<Int>(
                                "chatOtherUserId",
                                currentItem.user_id
                            )
                            navController.navigate(Screen.ChatDetail.route)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                    ) {
                        Text(text = "แชท", color = Color.White)
                    }
                }
            }
        }
    }
}

fun addFavorite(itemId: Int, currentIdUsers: Int, context: Context) {
    val createClient = kkurentApi.create()

    createClient.addFavorite(itemId, currentIdUsers).enqueue(object : Callback<FavoriteItem> {
        override fun onResponse(call: Call<FavoriteItem>, response: Response<FavoriteItem>) {
            if (response.isSuccessful) {
                val item = response.body()
                if (item != null) {
                    Toast.makeText(context, "เพิ่มไปยังรายการโปรดเรียบร้อย!", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(context, "Item not found", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        override fun onFailure(call: Call<FavoriteItem>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

fun showItemDetails(itemId: Int, context: Context, onDataReceived: (Item) -> Unit) {
    val createClient = kkurentApi.create()

    createClient.datailItems(itemId).enqueue(object : Callback<Item> {
        override fun onResponse(call: Call<Item>, response: Response<Item>) {
            if (response.isSuccessful) {
                val item = response.body()
                if (item != null) {
                    onDataReceived(item)
                } else {
                    Toast.makeText(context, "Item not found", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        override fun onFailure(call: Call<Item>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}
