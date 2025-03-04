package com.example.kkurent

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
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
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminApproval(navController: NavController) {
    val contextForToast = LocalContext.current

    val showApprovePostDialog = remember { mutableStateOf(false) }
    val showUnapprovePostDialog = remember { mutableStateOf(false) }

    val showApproveProductDialog = remember { mutableStateOf(false) }
    val showUnapproveProductDialog = remember { mutableStateOf(false) }

    var postList by remember { mutableStateOf<List<Post>>(emptyList()) }
    var productList by remember { mutableStateOf<List<Item>>(emptyList()) }

    var postId by remember { mutableStateOf(0) }
    var postName by remember { mutableStateOf("") }
    var productId by remember { mutableStateOf(0) }
    var productName by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        showAllNeedApprovePosts(contextForToast) { posts -> postList = posts }
        showAllNeedApproveProducts(contextForToast) { products -> productList = products }
    }

    if (showUnapproveProductDialog.value) {
        AlertDialog(
            onDismissRequest = { showUnapproveProductDialog.value = false },
            title = { Text("ไม่อนุมัติสินค้า") },
            text = { Text("คุณแน่ใจหรือไม่ว่าต้องการไม่อนุมัติสินค้านี้?") },
            confirmButton = {
                TextButton(onClick = {
                    unapproveProduct(productId, contextForToast) {
                        createNotification(
                            userId,
                            "สินค้าไม่ผ่านการอนุมัติ",
                            "สินค้าของคุณ '${productName}' ไม่ผ่านการอนุมัติ")
                        showAllNeedApproveProducts(contextForToast) { products -> productList = products }
                        showUnapproveProductDialog.value = false
                    }
                }) {
                    Text("ยืนยัน", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showUnapproveProductDialog.value = false }) {
                    Text("ยกเลิก")
                }
            }
        )
    }

    if (showApproveProductDialog.value) {
        AlertDialog(
            onDismissRequest = { showApproveProductDialog.value = false },
            title = { Text("อนุมัติสินค้า") },
            text = { Text("คุณแน่ใจหรือไม่ว่าต้องการอนุมัติสินค้านี้?") },
            confirmButton = {
                TextButton(onClick = {
                    approveProduct(productId, contextForToast) {
                        createNotification(
                            userId,
                            "สินค้าได้รับการอนุมัติแล้ว",
                            "สินค้าของคุณ '${productName}' ได้รับการอนุมัติเรียบร้อยแล้ว")
                        showAllNeedApproveProducts(contextForToast) { products -> productList = products }
                        showApproveProductDialog.value = false
                    }
                }) {
                    Text("ยืนยัน", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showApproveProductDialog.value = false }) {
                    Text("ยกเลิก")
                }
            }
        )
    }

    if (showUnapprovePostDialog.value) {
        AlertDialog(
            onDismissRequest = { showUnapprovePostDialog.value = false },
            title = { Text("ไม่อนุมัติโพสต์") },
            text = { Text("คุณแน่ใจหรือไม่ว่าต้องการไม่อนุมัติโพตส์นี้?") },
            confirmButton = {
                TextButton(onClick = {
                    unapprovePost(postId, contextForToast) {
                        createNotification(
                            userId,
                            "โพสต์ไม่ผ่านการอนุมัติ",
                            "โพสต์ของคุณ '${postName}' ไม่ผ่านการอนุมัติ")
                        showAllNeedApprovePosts(contextForToast) { posts -> postList = posts }
                        showUnapprovePostDialog.value = false
                    }
                }) {
                    Text("ยืนยัน", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showUnapprovePostDialog.value = false }) {
                    Text("ยกเลิก")
                }
            }
        )
    }

    if (showApprovePostDialog.value) {
        AlertDialog(
            onDismissRequest = { showApprovePostDialog.value = false },
            title = { Text("อนุมัติโพสต์") },
            text = { Text("คุณแน่ใจหรือไม่ว่าต้องการอนุมัติโพตส์นี้?") },
            confirmButton = {
                TextButton(onClick = {
                    approvePost(postId, contextForToast) {
                        createNotification(
                            userId,
                            "โพสต์ผ่านการอนุมัติ",
                            "โพสต์ของคุณ '${postName}' ผ่านการอนุมัตแล้วิ")
                        showAllNeedApprovePosts(contextForToast) { posts -> postList = posts }
                        showApprovePostDialog.value = false
                    }
                }) {
                    Text("ยืนยัน", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showApprovePostDialog.value = false }) {
                    Text("ยกเลิก")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MyTopAppBarAdminApproval(navController)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(10.dp).weight(1f)
        ) {
            item {
                Text(
                    text = "โพสต์ที่รออนุมัติ", // หัวข้อ
                    fontSize = 24.sp, // ขนาดใหญ่ขึ้น
                    fontWeight = FontWeight.Bold, // ตัวหนา
                    color = Color(0xFF0D346D),
                    modifier = Modifier.padding(bottom = 8.dp) // เพิ่มระยะห่างด้านล่าง
                )
            }
            if (postList.isEmpty()) {
                item {
                    Text(
                        text = "ยังไม่มีโพสต์่ที่รออนุมัติ",
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
                                    .width(150.dp)
                            )

                            Spacer(modifier = Modifier.width(3.dp))

                            Column(modifier = Modifier.weight(10f)) {
                                Text(
                                    text = "โพสต์โดย: ${item.fname_lname}",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "รายละเอียดโพสต์: ${item.post_detail}",
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    showUnapprovePostDialog.value = true
                                    postId = item.idPost
                                    postName = item.post_detail
                                    userId = item.user_id
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                modifier = Modifier.weight(1f).padding(end = 8.dp)
                            ) {
                                Text("ไม่อนุมัติ", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    showApprovePostDialog.value = true
                                    postId = item.idPost
                                    postName = item.post_detail
                                    userId = item.user_id
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF39B54A)),
                                modifier = Modifier.weight(1f).padding(start = 8.dp)
                            ) {
                                Text("อนุมัติ", color = Color.White)
                            }
                        }
                    }
                }
            }
            item {
                Text(
                    text = "สินค้าที่รออนุมัติ", // หัวข้อ
                    fontSize = 24.sp, // ขนาดใหญ่ขึ้น
                    fontWeight = FontWeight.Bold, // ตัวหนา
                    color = Color(0xFF0D346D),
                    modifier = Modifier.padding(bottom = 8.dp) // เพิ่มระยะห่างด้านล่าง
                )
            }
            if (productList.isEmpty()) {
                item {
                    Text(
                        text = "ยังไม่มีสินค้าที่รออนุมัติ",
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
                                        .width(150.dp)
                                )

                                Spacer(modifier = Modifier.width(3.dp))

                                Column(modifier = Modifier.weight(10f)) {
                                    Text(
                                        text = "โพสต์โดย: ${item.fname_lname}",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = item.item_name,
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        showUnapproveProductDialog.value = true
                                        productId = item.idItems
                                        productName = item.item_name
                                        userId = item.user_id
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                                ) {
                                    Text("ไม่อนุมัติ", color = Color.White)
                                }
                                Button(
                                    onClick = {
                                        showApproveProductDialog.value = true
                                        productId = item.idItems
                                        productName = item.item_name
                                        userId = item.user_id
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF39B54A)),
                                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                                ) {
                                    Text("อนุมัติ", color = Color.White)
                                }
                            }
                        }
                    }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBarAdminApproval(navController: NavController) {
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
                        text = "คำขออนุมัติ | ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFA655), // สีส้ม
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    NotificationButton(navController)
                }
            }
        )

    }
}

fun createNotification(userId: Int, title: String, message: String) {
    val createClient = kkurentApi.create()

    createClient.createNotification(userId, title, message).enqueue(object : Callback<Notification> {
        override fun onResponse(call: Call<Notification>, response: Response<Notification>) {
            if (response.isSuccessful) {
//                Toast.makeText(context, "อนุมัติโพสต์สำเร็จ!", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Notification>, t: Throwable) {
//            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

fun showAllNeedApproveProducts(context: Context, onDataReceived: (List<Item>) -> Unit) {
    val createClient = kkurentApi.create()

    createClient.needapproveitems().enqueue(object : Callback<Map<String, Any>> {
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

fun showAllNeedApprovePosts(context: Context, onDataReceived: (List<Post>) -> Unit) {
    val createClient = kkurentApi.create()

    createClient.needapproveposts().enqueue(object : Callback<Map<String, Any>> {
        override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
            if (response.isSuccessful) {
                val body = response.body()

                // ใช้ Gson แปลง data ให้เป็น List<Post>
                val gson = Gson()
                val jsonData = gson.toJson(body?.get("data")) // ดึงเฉพาะ data ออกมา
                val products: List<Post> = gson.fromJson(jsonData, object : TypeToken<List<Post>>() {}.type)

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

fun approvePost(postId: Int, context: Context, callback: () -> Unit) {
    val createClient = kkurentApi.create()

    createClient.approvePost(postId).enqueue(object : Callback<Post> {
        override fun onResponse(call: Call<Post>, response: Response<Post>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "อนุมัติโพสต์สำเร็จ!", Toast.LENGTH_SHORT).show()
                callback()
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<Post>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

fun unapprovePost(postId: Int, context: Context, callback: () -> Unit) {
    val createClient = kkurentApi.create()

    createClient.disapprovePost(postId).enqueue(object : Callback<Post> {
        override fun onResponse(call: Call<Post>, response: Response<Post>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "ไม่อนุมัติโพสต์สำเร็จ!", Toast.LENGTH_SHORT).show()
                callback()
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<Post>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

fun approveProduct(productId: Int, context: Context, callback: () -> Unit) {
    val createClient = kkurentApi.create()

    createClient.approveProduct(productId).enqueue(object : Callback<Item> {
        override fun onResponse(call: Call<Item>, response: Response<Item>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "อนุมัติสินค้าสำเร็จ!", Toast.LENGTH_SHORT).show()
                callback()
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<Item>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

fun unapproveProduct(productId: Int, context: Context , callback: () -> Unit) {
    val createClient = kkurentApi.create()

    createClient.disapproveProduct(productId).enqueue(object : Callback<Item> {
        override fun onResponse(call: Call<Item>, response: Response<Item>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "ไม่อนุมัติสินค้าสำเร็จ!", Toast.LENGTH_SHORT).show()
                callback()
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<Item>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}


