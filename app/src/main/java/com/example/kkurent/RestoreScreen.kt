package com.example.kkurent

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBarRestore(
    navController: NavController

) {
    val context = LocalContext.current
    val isUserRole = SharedPreferencesManager(context).role == "user"

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
                        text = if(isUserRole) "กู้คืน | " else "กู้คืน",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFA655), // สีส้ม
                        modifier = Modifier.padding(top = if(isUserRole) 10.dp else 18.dp, end = 10.dp)
                    )
                    if(isUserRole) {
                        NotificationButton(navController)
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestoreScreen(navController: NavHostController) {

    val contextForToast = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val showProductDialog = remember { mutableStateOf(false) }
    val showUsersDialog = remember { mutableStateOf(false) }
    var postList by remember { mutableStateOf<List<Post>>(emptyList()) }
    var productList by remember { mutableStateOf<List<Item>>(emptyList()) }
    var usersList by remember { mutableStateOf<List<Register>>(emptyList()) }
    var postId by remember { mutableStateOf(0) }
    var product_id by remember { mutableStateOf(0) }
    var userId by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        showAllPostDelete(contextForToast) { posts ->
            postList = posts // อัปเดต postList
        }
        showAllProductDelete(contextForToast) { products ->
            productList = products
        }
        showAllUsersDelete(contextForToast) { users ->
            usersList = users
        }
    }

    val showDeleteDialog = remember {
        mutableStateOf<Pair<Boolean,
                Int>?>(null)
    }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("คุณแน่ใจหรือว่าต้องการกู้คืนโพสต์นี้") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // กู้คืน
                        restorePost(postId, contextForToast);
                        showDialog.value = false
                        // ดึงข้อมูลใหม่
                        showAllPostDelete(contextForToast) { posts ->
                            postList = posts // อัปเดต postList
                        }
                    }
                ) {
                    Text("กู้คืน")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("ยกเลิก")
                }
            }
        )
    }

    if (showProductDialog.value) {
        AlertDialog(
            onDismissRequest = { showProductDialog.value = false },
            title = { Text("คุณแน่ใจหรือว่าต้องการกู้คืนสินค้านี้") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // กู้คืน
                        restoreProduct(product_id, contextForToast);
                        showProductDialog.value = false
                        // ดึงข้อมูลใหม่
                        showAllProductDelete(contextForToast) { products ->
                            productList = products // อัปเดต postList
                        }
                    }
                ) {
                    Text("กู้คืน")
                }
            },
            dismissButton = {
                TextButton(onClick = { showProductDialog.value = false }) {
                    Text("ยกเลิก")
                }
            }
        )
    }

    if (showUsersDialog.value) {
        AlertDialog(
            onDismissRequest = { showUsersDialog.value = false },
            title = { Text("คุณแน่ใจหรือว่าต้องการกู้คืนบัญชีผู้ใช้นี้") },
            confirmButton = {
                TextButton(
                    onClick = {
                        restoreUsers(userId, contextForToast) { restoredUsers ->
                            // อัปเดต usersList ทันทีหลังจากกู้คืนสำเร็จ
                            showAllUsersDelete(contextForToast) { users ->
                                usersList = users
                            }
                        }
                        showUsersDialog.value = false
                    }
                ) {
                    Text("กู้คืน")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUsersDialog.value = false }) {
                    Text("ยกเลิก")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MyTopAppBarRestore(navController)

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(10.dp).weight(1f)
        ) {
            // 🔹 หัวข้อโพสต์ที่ถูกลบ
            item {
                Text(
                    text = "โพสต์ที่ถูกลบ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D346D),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (postList.isEmpty()) {
                item {
                    Text(
                        text = "ยังไม่มีโพสต์ที่ถูกลบ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            } else {
                items(postList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(item.post_img),
                                contentDescription = "image",
                                modifier = Modifier
                                    .height(150.dp)
                                    .width(150.dp).clip(RoundedCornerShape(16.dp))
                                , contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(10f)) {
                                Text(
                                    text = "โพสต์โดย: ${item.fname_lname}",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "ลบเมื่อ: " + formatDateTime(item.deleteAt),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "รายละเอียดโพสต์: ${item.post_detail}",
                                    fontSize = 14.sp,
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            FloatingActionButton(
                                onClick = {
                                    postId = item.idPost
                                    showDialog.value = true
                                },
                                containerColor = Color(0xFFF5D049),
                                shape = CircleShape,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Restore Post",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // 🔹 หัวข้อสินค้าที่ถูกลบ
            item {
                Text(
                    text = "สินค้าที่ถูกลบ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D346D),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (productList.isEmpty()) {
                item {
                    Text(
                        text = "ยังไม่มีสินค้าที่ถูกลบ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            } else {
                items(productList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(item.item_img),
                                contentDescription = "image",
                                modifier = Modifier
                                    .height(150.dp)
                                    .width(150.dp).clip(RoundedCornerShape(16.dp))
                                , contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(10f)) {
                                Text(
                                    text = "โพสต์โดย: ${item.fname_lname}",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "ลบเมื่อ: " + formatDateTime(item.deleteAt),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = item.item_name,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            FloatingActionButton(
                                onClick = {
                                    product_id = item.idItems
                                    showProductDialog.value = true
                                },
                                containerColor = Color(0xFFF5D049),
                                shape = CircleShape,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Restore Product",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // 🔹 หัวข้อบัญชีผู้ใช้ที่ถูกลบ
            item {
                Text(
                    text = "บัญชีผู้ใช้ที่ถูกลบ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D346D),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (usersList.isEmpty()) {
                item {
                    Text(
                        text = "ยังไม่มีบัญชีผู้ใช้ที่ถูกลบ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            } else {
                items(usersList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(150.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(item.profile_picture),
                                contentDescription = "image",
                                modifier = Modifier
                                    .height(150.dp)
                                    .width(150.dp).clip(RoundedCornerShape(16.dp))
                                , contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(10f)) {
                                Text(
                                    text = "ชื่อบัญชีผู้ใช้ : ${item.fname_lname}",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "ลบเมื่อ: " + formatDateTime(item.deleteAt),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            FloatingActionButton(
                                onClick = {
                                    userId = item.idUsers
                                    showUsersDialog.value = true
                                },
                                containerColor = Color(0xFFF5D049),
                                shape = CircleShape,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Restore User",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


fun showAllPostDelete(context: Context, onDataReceived: (List<Post>) -> Unit) {
    val createClient = kkurentApi.create()

    createClient.deletedposts().enqueue(object : Callback<Map<String, Any>> {
        override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
            if (response.isSuccessful) {
                val body = response.body()

                // ใช้ Gson แปลง data ให้เป็น List<Post>
                val gson = Gson()
                val jsonData = gson.toJson(body?.get("data")) // ดึงเฉพาะ data ออกมา
                val posts: List<Post> = gson.fromJson(jsonData, object : TypeToken<List<Post>>() {}.type)

                onDataReceived(posts) // ส่งค่า List<Post> กลับไป
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

// เรียก api กู้คืน
fun restorePost(postId: Int, context: Context) {
    val createClient = kkurentApi.create()

    createClient.restorePost(postId).enqueue(object : Callback<Post> {
        override fun onResponse(call: Call<Post>, response: Response<Post>) {
            if (response.isSuccessful) {
                val item = response.body()
                if (item != null) {
                    Toast.makeText(context, "กู้คืนโพสต์สำเร็จ!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Item not found", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        override fun onFailure(call: Call<Post>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

// เรียก api กู้คืน
fun restoreProduct(productId: Int, context: Context) {
    val createClient = kkurentApi.create()

    createClient.restoreProduct(productId).enqueue(object : Callback<Item> {
        override fun onResponse(call: Call<Item>, response: Response<Item>) {
            if (response.isSuccessful) {
                val item = response.body()
                if (item != null) {
                    Toast.makeText(context, "กู้คืนสินค้าสำเร็จ!", Toast.LENGTH_LONG).show()
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


fun showAllProductDelete(context: Context, onDataReceived: (List<Item>) -> Unit) {
    val createClient = kkurentApi.create()

    createClient.deletedItems().enqueue(object : Callback<Map<String, Any>> {
        override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
            if (response.isSuccessful) {
                val body = response.body()

                // ใช้ Gson แปลง data ให้เป็น List<Item>
                val gson = Gson()
                val jsonData = gson.toJson(body?.get("data")) // ดึงเฉพาะ data ออกมา
                val products: List<Item> = gson.fromJson(jsonData, object : TypeToken<List<Item>>() {}.type)

                onDataReceived(products) // ส่งค่า List<Post> กลับไป
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}




fun showAllUsersDelete(context: Context, onDataReceived: (List<Register>) -> Unit) {
    val createClient = userApi.create()

    createClient.deletedusers().enqueue(object : Callback<Map<String, Any>> {
        override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
            if (response.isSuccessful) {
                val body = response.body()

                // ใช้ Gson แปลง data ให้เป็น List<Item>
                val gson = Gson()
                val jsonData = gson.toJson(body?.get("data")) // ดึงเฉพาะ data ออกมา
                val users: List<Register> = gson.fromJson(jsonData, object : TypeToken<List<Register>>() {}.type)

                onDataReceived(users) // ส่งค่า List<Post> กลับไป
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

fun restoreUsers(userId: Int, context: Context, onDataReceived: (List<Register>) -> Unit) {
    val createClient = userApi.create()

    createClient.restoreUsers(userId).enqueue(object : Callback<Register> {
        override fun onResponse(call: Call<Register>, response: Response<Register>) {
            if (response.isSuccessful) {
                val user = response.body()

                if (user != null && user.del_status != "Y") {
                    onDataReceived(listOf(user)) // ส่งค่ากลับเป็น List<Register>
                    Toast.makeText(context, "กู้คืนบัญชีผู้ใช้สำเร็จ!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "User not found หรือถูกลบ", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<Register>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}


