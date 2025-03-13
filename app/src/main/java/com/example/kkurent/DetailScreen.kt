package com.example.kkurent

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    val sharedPreferences = SharedPreferencesManager(context)
    var item by remember { mutableStateOf<Item?>(null) }
    var isFavorite by remember { mutableStateOf(false) }
    val userRole = sharedPreferences.role
    var items_id by remember { mutableStateOf(0) }
    var softDeleteDialog by remember { mutableStateOf(false) }

    // ดึงข้อมูลสินค้าเมื่อเปิดหน้า
    LaunchedEffect(Unit) {
        showItemDetails(itemId, context) { retrievedItem ->
            item = retrievedItem
        }
        checkFavorite(itemId, SharedPreferencesManager(context).idUsers, context) {
            isFavorite = it
        }
    }

    // แสดงรายละเอียดสินค้า
    item?.let { currentItem ->
        val isCurrentUser = sharedPreferences.isCurrentUser(currentItem.user_id)
        val imageHeight = 300.dp // ความสูงของรูปภาพ

        if (softDeleteDialog) {
            AlertDialog(
                onDismissRequest = { softDeleteDialog = false },
                text = { Text("คุณต้องการลบสินค้าใช่หรือไม่") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            kkurentApi.create().softDeleteItems(items_id).enqueue(object : Callback<Item> {
                                override fun onResponse(call: Call<Item>, response: Response<Item>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(context, "ลบสินค้าสำเร็จ!", Toast.LENGTH_SHORT)
                                            .show()
                                        softDeleteDialog = false
                                        navController.navigate(Screen.Products.route)
                                    } else {
                                        Toast.makeText(context, "ลบสินค้าไม่สำเร็จ!", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }

                                override fun onFailure(call: Call<Item>, t: Throwable) {
                                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT)
                                        .show()
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

        Box(modifier = Modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
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
                  actions = {
                      if (userRole == "user" && isCurrentUser) {
                          ActionIcon(
                              imageVector = Icons.Default.Edit,
                              contentDescription = "Edit Item",
                                 tint = Color(0xFFFFA655), // สีส้ม
                          ) {
                              navController.currentBackStackEntry?.savedStateHandle?.set("data", currentItem)
                              navController.navigate(Screen.EditItem.route)
                          }
                      }
                      if (userRole == "admin" || userRole == "user" && isCurrentUser) {
                          ActionIcon(
                              imageVector = Icons.Filled.Delete,
                              contentDescription = "Delete Icon",
                              tint = Color.Red
                          ) {
                              items_id = currentItem.idItems
                              softDeleteDialog = true
                          }
                      }
                  },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }

            // รูปภาพ (อยู่กับที่ ไม่เลื่อน)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight) // กำหนดความสูงของ Image ให้เท่ากับ Header
                    .align(Alignment.Center)
                    .offset(y = -200.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(currentItem.item_img),
                    contentDescription = "Item Image",
                    modifier = Modifier
                        .size(250.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            }

            // LazyColumn (สามารถเลื่อนได้)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = imageHeight + 58.dp + 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    ) // เริ่มต้นหลัง Image
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(currentItem.profilePicture),
                                contentDescription = currentItem.item_name,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                text = if (isCurrentUser) "สินค้าของคุณ" else currentItem.fname_lname,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            if (isCurrentUser) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "my item",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(12.dp),
                                )
                            }
                        }
                        Text(
                            text = formatDateTime(currentItem.createAt),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                item {
                    Text(
                        text = currentItem.item_name,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                item {
                    Text(
                        text = currentItem.item_detail,
                        fontSize = 16.sp,
                    )
                }
                item {
                    // หมวดหมู่
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = "Category Name",
                            tint = Color.Magenta,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "หมวดหมู่: ${currentItem.category_name}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                item {
                    // ที่ตั้งสินค้า
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location Icon",
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "สถานที่: ${currentItem.location}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                item {
                    // สถานะสินค้า
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Status Icon",
                            tint = Color.Blue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "สถานะ: ${currentItem.item_status}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                item {
                    Text(
                        text = "ราคา: ${currentItem.price} บาท ต่อวัน",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF003366),
                    )
                }
                item {
                    // ปุ่มต่างๆ
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if(isFavorite) {
                                    deleteFavorite(itemId, sharedPreferences.idUsers, context) {
                                        // เช็คการกดสินค้าโปรด
                                        checkFavorite(itemId, SharedPreferencesManager(context).idUsers, context) {
                                            isFavorite = it
                                        }
                                    }
                                } else {
                                    addFavorite(itemId, sharedPreferences.idUsers, context) {
                                        // เช็คการกดสินค้าโปรด
                                        checkFavorite(itemId, SharedPreferencesManager(context).idUsers, context) {
                                            isFavorite = it
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isFavorite) Color.Gray else Color.Red)
                        ) {
                            Text(text = if (isFavorite) "รายการโปรดแล้ว" else "รายการโปรด", color = Color.White)
                        }
                        if (!isCurrentUser) {
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set<Int>(
                                        "chatOtherUserId",
                                        currentItem.user_id
                                    )
                                    navController.navigate(Screen.ChatDetail.route)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFFFF9800
                                    )
                                )
                            ) {
                                Text(text = "แชท", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun addFavorite(itemId: Int, currentIdUsers: Int, context: Context, callback: () -> Unit) {
    val createClient = kkurentApi.create()

    createClient.addFavorite(itemId, currentIdUsers).enqueue(object : Callback<Item> {
        override fun onResponse(call: Call<Item>, response: Response<Item>) {
            if (response.isSuccessful) {
                val item = response.body()
                if (item != null) {
                    Toast.makeText(context, "เพิ่มไปยังรายการโปรดเรียบร้อย!", Toast.LENGTH_LONG)
                        .show()
                    callback()
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

fun checkFavorite(itemId: Int, userId: Int, context: Context, callback: (Boolean) -> Unit) {
    kkurentApi.create().checkFavorite(itemId, userId).enqueue(object : Callback<Boolean> {
        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
            if (response.isSuccessful) {
                callback(response.body() == true)
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG)
                    .show()
            }
        }
        override fun onFailure(call: Call<Boolean>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

fun deleteFavorite(productId: Int, userId: Int, context: Context, callback: () -> Unit) {
    val createClient = kkurentApi.create()

    createClient.deleteFavorite(productId, userId).enqueue(object : Callback<Item> {
        override fun onResponse(call: Call<Item>, response: Response<Item>) {
            if (response.isSuccessful) {
                val item = response.body()
                if (item != null) {
                    Toast.makeText(context, "ลบจากรายการโปรดเรียบร้อย!", Toast.LENGTH_LONG).show()
                    callback()
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