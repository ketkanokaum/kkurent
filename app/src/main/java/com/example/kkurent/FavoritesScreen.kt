package com.example.kkurent

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBarFavorites(
    navController: NavController
) {
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
                        text = "รายการโปรด  | ",
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


@Composable
fun FavoritesScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = SharedPreferencesManager(context)
    var itemList by remember { mutableStateOf(listOf<Item>()) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Item?>(null) }
    var items_id by remember { mutableStateOf(0) }
    var softDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (sharedPreferences.isLoggedIn) {
            showAllFavoriteItem(context, sharedPreferences.idUsers) { newItemList ->
                itemList = newItemList
            }
        }
    }
    //ทั้งหน้า
    Box(modifier = Modifier.fillMaxSize()) {
        MyTopAppBarFavorites(navController)
    }

    if (softDeleteDialog) {
        AlertDialog(
            onDismissRequest = { softDeleteDialog = false },
            text = { Text("คุณต้องการลบสินค้าใช่หรือไม่") },
            confirmButton = {
                TextButton(
                    onClick = {
                        kkurentApi.create().softDeleteItems(items_id)
                            .enqueue(object : Callback<Item> {
                                override fun onResponse(
                                    call: Call<Item>,
                                    response: Response<Item>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            "ลบสินค้าสำเร็จ!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        itemList = itemList.filter { it.idItems != items_id }
                                        softDeleteDialog = false
                                        navController.popBackStack(Screen.Products.route, false)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "ลบสินค้าไม่สำเร็จ!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }

                                override fun onFailure(call: Call<Item>, t: Throwable) {
                                    Toast.makeText(
                                        context,
                                        "Error: ${t.message}",
                                        Toast.LENGTH_SHORT
                                    )
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


    if (itemList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "ไม่มีรายการโปรด",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    } else {
        Column(
            modifier = Modifier.padding(top = 105.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item(span = { GridItemSpan(2) }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "รายการโปรดทั้งหมด",
                                fontSize = 20.sp,
                                color = Color(0xFF0D346D),
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "All Favorite",
                                tint = Color(0xFF0D346D),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "${itemList.size} รายการ",
                            color = Color.Gray,
                            fontSize = 16.sp,
                        )
                    }
                }
                items(itemList) { item ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        ProductItem(item, {
                            items_id = item.idItems
                            softDeleteDialog = true
                        }, navController)
                        IconButton(onClick = {
                            selectedItem = item
                            showDialog = true
                        }, modifier = Modifier.align(Alignment.TopStart)) {
                            Icon(
                                painter = painterResource(id = R.drawable.heart_filled),
                                contentDescription = "Remove Favorite",
                                tint = Color.Red,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                    }

                }
                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(0.dp))
                }
            }
        }

        // AlertDialog สำหรับยืนยันการลบรายการโปรด
        if (showDialog && selectedItem != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("ลบรายการโปรด") },
                text = { Text("คุณแน่ใจหรือไม่ว่าต้องการลบ ${selectedItem!!.item_name} ออกจากรายการโปรด?") },
                confirmButton = {
                    TextButton(onClick = {
                        selectedItem?.let { item ->
                            deleteFavorite(item.idItems, sharedPreferences.idUsers, context) {
                                showAllFavoriteItem(context, sharedPreferences.idUsers) { newItemList ->
                                    itemList = newItemList
                                }
                            }
                        }
                        showDialog = false
                    }) {
                        Text("ยืนยัน", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("ยกเลิก")
                    }
                }
            )
        }
    }
}

fun showAllFavoriteItem(
    context: Context,
    currentUserId: Int,
    onDataReceived: (List<Item>) -> Unit
) {
    val createClient = kkurentApi.create()

    createClient.getFavorite(currentUserId).enqueue(object : Callback<List<Item>> {
        override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
            if (response.isSuccessful) {
                val items = response.body() ?: emptyList()
                onDataReceived(items)
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        override fun onFailure(call: Call<List<Item>>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}