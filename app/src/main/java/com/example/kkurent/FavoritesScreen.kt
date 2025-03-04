package com.example.kkurent

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
    var itemList by remember { mutableStateOf(listOf<FavoriteItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<FavoriteItem?>(null) }

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

        Column( verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 120.dp)) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(itemList) { item ->
                    FavoriteItemCard(item, onRemove = {
                        selectedItem = item
                        showDialog = true
                    })
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
                        selectedItem?.let {
                            deleteFavorite(it.idFavorites, context)
                            showAllFavoriteItem(context, sharedPreferences.idUsers) { newItemList ->
                                itemList = newItemList
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


@Composable
fun FavoriteItemCard(item: FavoriteItem, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier.padding(16.dp).fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Image(
                    painter = if (item.item_img != "no image") {
                        rememberAsyncImagePainter(item.item_img)
                    } else {
                        painterResource(id = R.drawable.image_search)
                    },
                    contentDescription = item.item_name,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = item.item_name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "ราคาเช่าสินค้า : ${item.price} บาท/ต่อวัน", fontSize = 14.sp, color = Color(0xFFF59949))
                Spacer(modifier = Modifier.height(8.dp))
            }
            Icon(
                painter = painterResource(id = R.drawable.heart_filled),
                contentDescription = "Remove Favorite",
                tint = Color.Red,
                modifier = Modifier.size(24.dp).align(Alignment.TopEnd).clickable { onRemove() }
            )
        }
    }
}

fun showAllFavoriteItem(context: Context, currentUserId: Int, onDataReceived: (List<FavoriteItem>) -> Unit) {
    val createClient = kkurentApi.create()

    createClient.getFavorite(currentUserId).enqueue(object : Callback<List<FavoriteItem>> {
        override fun onResponse(call: Call<List<FavoriteItem>>, response: Response<List<FavoriteItem>>) {
            if (response.isSuccessful) {
                val items = response.body() ?: emptyList()
                onDataReceived(items)
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        override fun onFailure(call: Call<List<FavoriteItem>>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

fun deleteFavorite(idFavorites: Int, context: Context) {
    val createClient = kkurentApi.create()

    createClient.deleteFavorite(idFavorites).enqueue(object : Callback<FavoriteItem> {
        override fun onResponse(call: Call<FavoriteItem>, response: Response<FavoriteItem>) {
            if (response.isSuccessful) {
                val item = response.body()
                if (item != null) {
                    Toast.makeText(context, "ลบจากรายการโปรดเรียบร้อย!", Toast.LENGTH_LONG).show()
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