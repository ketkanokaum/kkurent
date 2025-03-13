package com.example.kkurent

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = SharedPreferencesManager(context)

    // ดึงข้อมูลจาก SharedPreferences
    val sharedPreferences_role = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    //val idUsers = sharedPreferences.idUsers
    var fullName = sharedPreferences.fnameLname ?: ""
    var email = sharedPreferences.email ?: ""
    var phone = sharedPreferences.phone ?: ""
    var address = sharedPreferences.address ?: ""
    var bank = sharedPreferences.bank ?: ""
    var accountNumber = sharedPreferences.accountNumber ?: ""
    var accountName = sharedPreferences.accountName ?: ""
    var status = sharedPreferences.del_status ?: ""
    val createClient = userApi.create()
    var logoutDialog by remember { mutableStateOf(false) }
    var checkedState by remember { mutableStateOf(false) }
    val initialUser = Register(0, "", "", "", "", "", "", "", "", "", "", " ", "", "", "") // ข้อมูลเริ่มต้น
    var userItems by remember { mutableStateOf(initialUser) }
    val contextForToast = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    val userRole = sharedPreferences_role.getString("key_role", "")
    var postList by remember { mutableStateOf(listOf<Post>()) }
    var productList by remember { mutableStateOf(listOf<Item>()) }

    var showEditDialog by remember { mutableStateOf(false) }

    val idUsers = sharedPreferences.idUsers

    var postId by remember { mutableIntStateOf(0) }
    var items_id by remember { mutableIntStateOf(0) }
    var softDeletePostDialog by remember { mutableStateOf(false) }
    var softDeleteProductDialog by remember { mutableStateOf(false) }

    LaunchedEffect(lifecycleState) {
        Log.d("LifecycleState", "Current state: $lifecycleState")
        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> Log.d("LifecycleState", "DESTROYED state")
            Lifecycle.State.INITIALIZED -> Log.d("LifecycleState", "INITIALIZED state")
            Lifecycle.State.CREATED -> Log.d("LifecycleState", "CREATED state")
            Lifecycle.State.STARTED -> Log.d("LifecycleState", "STARTED state")
            Lifecycle.State.RESUMED -> {
                Log.d("LifecycleState", "RESUMED state - Fetching user data")
                createClient.searchUser(idUsers).enqueue(object : Callback<Register> {
                    override fun onResponse(call: Call<Register>, response: Response<Register>) {
                        Log.d("API Response", "Response received: ${response.code()}")
                        if (response.isSuccessful) {
                            response.body()?.let { user ->
                                userItems = Register(
                                    user.idUsers, user.fname_lname, user.email, user.address,
                                    user.bank, user.account_number, user.account_name,
                                    user.password, user.phone, user.profile_picture,
                                    user.id_card_img, user.role, user.del_status, user.createAt, user.deleteAt
                                )
                                Log.d("API Response", "User data fetched successfully: $user")
                            }
                        } else {
                            Log.e("API Response", "User ID Not Found, Code: ${response.code()}")
                            Toast.makeText(contextForToast, "User ID Not Found", Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<Register>, t: Throwable) {
                        Log.e("API Response", "Error onFailure: ${t.message}")
                        Toast.makeText(contextForToast, "Error: ${t.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                })
                val createKkurentClient = kkurentApi.create()
                createKkurentClient.retrieveUserPosts(idUsers)
                    .enqueue(object : Callback<List<Post>> {
                        override fun onResponse(
                            call: Call<List<Post>>,
                            response: Response<List<Post>>
                        ) {
                            Log.d("API Response", "Response received: ${response.code()}")
                            if (response.isSuccessful) {
                                val items = response.body() ?: emptyList()
                                postList = items
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error: ${response.message()}",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                            Toast.makeText(
                                context,
                                "Error onFailure: ${t.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                createKkurentClient.retrieveUserProducts(idUsers)
                    .enqueue(object : Callback<List<Item>> {
                        override fun onResponse(
                            call: Call<List<Item>>,
                            response: Response<List<Item>>
                        ) {
                            Log.d("API Response", "Response received: ${response.code()}")
                            if (response.isSuccessful) {
                                val items = response.body() ?: emptyList()
                                productList = items
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error: ${response.message()}",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                            Toast.makeText(
                                context,
                                "Error onFailure: ${t.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            }

            else -> Log.d("LifecycleState", "Unhandled state")
        }
    }

    if (softDeletePostDialog) {
        AlertDialog(
            onDismissRequest = { softDeletePostDialog = false },
            text = { Text("คุณต้องการลบโพสต์นี้ใช่หรือไม่") },
            confirmButton = {
                TextButton(
                    onClick = {
                        kkurentApi.create().softDeletePost(postId).enqueue(object : Callback<Post> {
                            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "ลบโพสต์สำเร็จ!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    postList = postList.filter { it.idPost != postId }
                                    softDeletePostDialog = false
                                } else {
                                    Toast.makeText(
                                        context,
                                        "ลบโพสต์ไม่สำเร็จ!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<Post>, t: Throwable) {
                                Toast.makeText(
                                    context,
                                    "Error: ${t.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    }
                ) {
                    Text("ใช่")
                }
            },
            dismissButton = {
                TextButton(onClick = { softDeletePostDialog = false }) {
                    Text("ไม่")
                }
            }
        )
    }

    if (softDeleteProductDialog) {
        AlertDialog(
            onDismissRequest = { softDeleteProductDialog = false },
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
                                        productList = productList.filter { it.idItems != items_id }
                                        softDeleteProductDialog = false
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
                TextButton(onClick = { softDeleteProductDialog = false }) {
                    Text("ไม่")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Button(
                onClick = {
                    showEditDialog = true
                }) {
                Text(text = "แก้ไขโปรไฟล์")
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            if (showEditDialog) {
                EditProfileDialog(
                    userItems = userItems,
                    onDismiss = { showEditDialog = false },
                    onSave = { newFnameLname, newEmail, newAddress, newBank, newAccountNumber, newAccountName, newPhone, newProfileImage, newIdCardImage ->
                        showEditDialog = false
                        updateUserProfile(
                            newFnameLname,
                            newEmail,
                            newAddress,
                            newBank,
                            newAccountNumber,
                            newAccountName,
                            newPhone,
                            newProfileImage,
                            newIdCardImage,
                            context
                        ) {
                            // ✅ โหลดข้อมูลใหม่เมื่ออัปเดตสำเร็จ
                            createClient.searchUser(idUsers).enqueue(object : Callback<Register> {
                                override fun onResponse(
                                    call: Call<Register>,
                                    response: Response<Register>
                                ) {
                                    if (response.isSuccessful) {
                                        response.body()?.let { user ->
                                            userItems = user // ✅ อัปเดตค่า userItems
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<Register>, t: Throwable) {
                                    Log.e("ProfileScreen", "Error fetching updated user data")
                                }
                            })
                        }
                    }
                )
            }
        }






        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F1F9))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // รูปโปรไฟล์
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(Color.Blue),
                        contentAlignment = Alignment.Center
                    ) {

                        Image(
                            painter = if (userItems.profile_picture != "no image") {
                                rememberAsyncImagePainter(userItems.profile_picture)
                            } else {
                                painterResource(id = R.drawable.image_search)
                            },
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                        )

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "รูปบัตรประชาชน", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    // รูปบััตรชาชน
                    Box(
                        modifier = Modifier
                            .aspectRatio(16f / 9f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = if (userItems.id_card_img != "no image") {
                                rememberAsyncImagePainter(userItems.id_card_img)
                            } else {
                                painterResource(id = R.drawable.image_search)
                            },
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize() // Fill the Box while maintaining 16:9
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // แสดงข้อมูลผู้ใช้
                    // ใช้ userItems ที่ได้จาก API ถ้ามีข้อมูล ถ้าไม่มีให้ใช้ค่าเดิมจาก SharedPreferences
                    ProfileInfoRow("ชื่อนามสกุล", userItems.fname_lname.ifEmpty { fullName })
                    ProfileInfoRow("อีเมล", userItems.email.ifEmpty { email })
                    if (userItems.role == "user" || userItems.del_status == "N") {
                        ProfileInfoRow("เบอร์โทร", userItems.phone.ifEmpty { phone })
                        ProfileInfoRow("ที่อยู่", userItems.address.ifEmpty { address })
                        ProfileInfoRow("ธนาคาร", userItems.bank.ifEmpty { bank })
                        ProfileInfoRow(
                            "เลขบัญชี",
                            userItems.account_number.ifEmpty { accountNumber })
                        ProfileInfoRow("ชื่อบัญชี", userItems.account_name.ifEmpty { accountName })
                    }
                    ProfileInfoRow("บทบาท", normalizeRole(userItems.role))
                }
            }
        }

        if (userRole == "user") {
            // 🔹 ส่วนโพสต์ของผู้ใช้
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "โพสต์ของคุณ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D346D)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (postList.isEmpty()) {
                item {
                    Text(text = "ไม่มีโพสต์ของคุณ")
                }
            } else {
                // แสดงรายการโพสต์สินค้า
                items(postList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                painter = rememberAsyncImagePainter(item.post_img),
                                contentDescription = "image",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.post_detail,
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    IconButton(onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "data",
                                            Post(
                                                item.idPost,
                                                item.post_detail,
                                                item.post_img,
                                                item.user_id,
                                                item.fname_lname,
                                                item.createAt,
                                                item.profilePicture,
                                                item.deleteAt
                                            )
                                        )
                                        navController.navigate(Screen.EditPost.route)

                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Post",
                                            modifier = Modifier.size(24.dp) // ปรับขนาดไอคอน
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            postId = item.idPost // กำหนดค่า postId
                                            softDeletePostDialog = true // เปิด Dialog
                                        },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Delete Icon",
                                            tint = Color.Red,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "สินค้าของคุณ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D346D)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (productList.isEmpty()) {
                item {
                    Text(text = "ไม่มีสินค้าของคุณ")
                }
            } else {
                items(productList) { item ->
                    // ใช้ state เฉพาะสำหรับแต่ละ item
                    var status by remember { mutableStateOf(item.item_status) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "idItems",
                                item.idItems
                            )
                            navController.navigate("detail/${item.idItems}")
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // รูปสินค้า
                            val imageUrl = item.item_img
                            if (!imageUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = "Item Image",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                )
                            } else {
                                Text(text = " ", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = item.item_name,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    // ปุ่มเปลี่ยนสถานะสินค้า
                                    Button(
                                        onClick = {
                                            val newStatus =
                                                if (status == "ว่าง") "ไม่ว่าง" else "ว่าง"
                                            updateItemStatus(item.idItems, newStatus) { success ->
                                                if (success) {
                                                    status = newStatus  // อัปเดตค่าใน UI
                                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                                        "updatedStatus_${item.idItems}",
                                                        newStatus
                                                    )
                                                } else {
                                                    Log.e(
                                                        "ItemStatus",
                                                        "Update failed for item id ${item.idItems}"
                                                    )
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (status == "ว่าง") Color(0xFFF59949) else Color.Gray
                                        ),
                                    ) {
                                        Text(text = if (status == "ว่าง") "ว่างให้เช่า" else "ถูกเช่าอยู่")

                                    }
                                    // ปุ่ม "แก้ไข"
                                    IconButton(onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "data",
                                            Item(
                                                item.idItems,
                                                item.item_name,
                                                item.item_detail,
                                                item.price,
                                                item.item_img,
                                                item.location,
                                                item.category_name,
                                                item.fname_lname,
                                                status,  // ใช้ status ปัจจุบัน
                                                item.user_id,
                                                item.profilePicture,
                                                item.createAt,
                                                item.deleteAt
                                            )
                                        )
                                        Log.d("Debug", "Item data: $item")
                                        navController.navigate(Screen.EditItem.route)

                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Post",
                                            modifier = Modifier.size(24.dp) // ปรับขนาดไอคอน
                                        )
                                    }
                                    ActionIcon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete Icon",
                                        tint = Color.Red
                                    ) {
                                        items_id = item.idItems
                                        softDeleteProductDialog = true
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        } else {
            item {
                Button(
                    onClick = { navController.navigate(Screen.ShowAllUsers.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "แสดงผู้ใช้", fontSize = 18.sp)
                }
            }
        }

        // 🔹 ปุ่ม Logout
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { logoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 8.dp),
            ) {
                Text(text = "ออกจากระบบ", fontSize = 18.sp)
            }
        }
    }

    // 🔹 กล่องยืนยันออกจากระบบ
    if (logoutDialog) {
        AlertDialog(
            onDismissRequest = {
                logoutDialog = false
            },
            title = { Text(text = "ออกจากระบบ") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "คุณแน่ใจหรือไม่ว่าต้องการออกจากระบบ?")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Checkbox(
                            checked = checkedState,
                            onCheckedChange = { isChecked -> checkedState = isChecked }
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "จำอีเมลของฉันไว้")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        logoutDialog = false
                        if (checkedState) {
                            sharedPreferences.clearUserAll()
                            Toast.makeText(
                                contextForToast,
                                "ออกจากระบบเรียบร้อยแล้ว",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.currentBackStackEntry?.savedStateHandle?.set<Int>(
                                "selectedScreen",
                                0
                            )
                        } else {
                            sharedPreferences.clearUserLogin()
                            Toast.makeText(
                                contextForToast,
                                "ล้างข้อมูลเข้าสู่ระบบและอีเมลแล้ว",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.currentBackStackEntry?.savedStateHandle?.set<Int>(
                                "selectedScreen",
                                0
                            )
                        }
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Profile.route) { inclusive = true }
                        }
                    }
                ) {
                    Text(text = "ใช่")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        logoutDialog = false
                        Toast.makeText(
                            contextForToast,
                            "ยกเลิกออกจากระบบ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Text(text = "ไม่")
                }
            }
        )
    }
}

fun normalizeRole(role: String): String {
    return if (role == "admin") {
        "แอดมิน"
    } else if (role == "user") {
        "ผู้ใช้งาน"
    } else {
        ""
    }
}

fun updateItemStatus(idItems: Int, newStatus: String, callback: (Boolean) -> Unit) {
    val api = kkurentApi.create()
    val request = UpdateStatusRequest(item_status = newStatus)

    Log.d("ItemStatus", "Sending request to update item ID: $idItems with status: $newStatus")

    api.updateItemStatus(idItems, request).enqueue(object : Callback<Item> {
        override fun onResponse(call: Call<Item>, response: Response<Item>) {
            if (response.isSuccessful) {
                Log.d("ItemStatus", "Updated successfully: ${response.body()}")
                callback(true)
            } else {
                Log.e("ItemStatus", "Failed to update: ${response.errorBody()?.string()}")
                callback(false)
            }
        }

        override fun onFailure(call: Call<Item>, t: Throwable) {
            Log.e("ItemStatus", "Error updating item status", t)
            callback(false)
        }
    })
}


@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = value, fontSize = 16.sp, color = Color.DarkGray)
    }
}


@Composable
fun EditProfileDialog(
    userItems: Register,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String, String, String, Uri?, Uri?) -> Unit
) {
    var newFnameLname by remember { mutableStateOf(userItems.fname_lname) }
    var newEmail by remember { mutableStateOf(userItems.email) }
    var newPhone by remember { mutableStateOf(userItems.phone) }
    var newAddress by remember { mutableStateOf(userItems.address) }
    val newBank = remember { mutableStateOf(userItems.bank) }
    var newAccountNumber by remember { mutableStateOf(userItems.account_number) }
    var newAccountName by remember { mutableStateOf(userItems.account_name) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var idCardUri by remember { mutableStateOf<Uri?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("แก้ไขข้อมูลโปรไฟล์") },
        text = {
            Column {
                ImagePicker(
                    "รูปโปรไฟล์",
                    profileImageUri?.toString() ?: userItems.profile_picture
                ) {
                    profileImageUri = it
                }
                ImagePicker("บัตรประชาชน", idCardUri?.toString() ?: userItems.id_card_img) {
                    idCardUri = it
                }
                TextField(
                    value = newFnameLname,
                    onValueChange = { newFnameLname = it },
                    label = { Text("ชื่อ–นามสกุล") })
                TextField(
                    value = newEmail,
                    onValueChange = { newEmail = it },
                    label = { Text("อีเมล") })
                TextField(
                    value = newPhone,
                    onValueChange = { newPhone = it },
                    label = { Text("เบอร์โทร") })
                TextField(
                    value = newAddress,
                    onValueChange = { newAddress = it },
                    label = { Text("ที่อยู่") })
                ProfileBankDropdown(newBank)
                TextField(
                    value = newAccountNumber,
                    onValueChange = { newAccountNumber = it },
                    label = { Text("เลขบัญชี") })
                TextField(
                    value = newAccountName,
                    onValueChange = { newAccountName = it },
                    label = { Text("ชื่อบัญชี") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(
                    newFnameLname, newEmail, newPhone, newAddress,
                    newBank.value, newAccountNumber, newAccountName, profileImageUri, idCardUri
                )
            }) {
                Text("บันทึกการเปลี่ยนแปลง")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("ยกเลิก")
            }
        }
    )
}

fun createImagePart(uri: Uri?, key: String, context: Context): MultipartBody.Part? {
    if (uri == null || uri.toString().isEmpty()) return null // ✅ Handle null or empty URI safely

    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: return null // ✅ Handle failure to open stream safely

        val file = File.createTempFile("temp", ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(file)

        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()

        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        MultipartBody.Part.createFormData(key, file.name, requestBody)

    } catch (e: Exception) {
        e.printStackTrace()
        null // ✅ Return null if any error occurs
    }
}

// ฟังก์ชันอัปเดตข้อมูลไปยังเซิร์ฟเวอร์
fun updateUserProfile(
    fnamelname: String,
    email: String,
    phone: String,
    address: String,
    bank: String,
    accountNumber: String,
    accountName: String,
    profileImageUri: Uri?,
    idCardImageUri: Uri?,
    context: Context,
    onUpdateSuccess: () -> Unit  // ✅ เพิ่ม callback ให้รู้ว่าอัปเดตสำเร็จ
) {
    val api = kkurentApi.create()
    val userId = SharedPreferencesManager(context).idUsers

    val profilePicturePart: MultipartBody.Part? =
        createImagePart(profileImageUri, "profile_picture", context)
    val idCardImgPart: MultipartBody.Part? = createImagePart(idCardImageUri, "id_card_img", context)

    val userIdPart = userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    val fnameLnamepart = fnamelname.toRequestBody("text/plain".toMediaTypeOrNull())
    val emailPart = email.toRequestBody("text/plain".toMediaTypeOrNull())
    val addressPart = address.toRequestBody("text/plain".toMediaTypeOrNull())
    val bankPart = bank.toRequestBody("text/plain".toMediaTypeOrNull())
    val accountNumberPart = accountNumber.toRequestBody("text/plain".toMediaTypeOrNull())
    val accountNamePart = accountName.toRequestBody("text/plain".toMediaTypeOrNull())
    val phonePart = phone.toRequestBody("text/plain".toMediaTypeOrNull())

    api.updateProfile(
        userIdPart,
        fnameLnamepart,
        emailPart,
        addressPart,
        bankPart,
        accountNumberPart,
        accountNamePart,
        phonePart,
        profilePicturePart,
        idCardImgPart,
    )
        .enqueue(object : Callback<UpdateProfileResponse> {
            override fun onResponse(
                call: Call<UpdateProfileResponse>,
                response: Response<UpdateProfileResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "อัปเดตข้อมูลสำเร็จ!", Toast.LENGTH_SHORT).show()
                    onUpdateSuccess()  // ✅ เรียกฟังก์ชันดึงข้อมูลใหม่
                } else {
                    Toast.makeText(context, "เกิดข้อผิดพลาด!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                Toast.makeText(context, "เชื่อมต่อเซิร์ฟเวอร์ไม่สำเร็จ!", Toast.LENGTH_SHORT).show()
            }
        })
}

fun uriToMultipart(uri: Uri, paramName: String, context: Context): MultipartBody.Part? {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri) ?: return null
    val file = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")

    file.outputStream().use { outputStream ->
        inputStream.copyTo(outputStream)
    }

    val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(paramName, file.name, requestBody)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBankDropdown(selectedBank: MutableState<String>) {
    val bankList = listOf(
        "ธนาคารกรุงไทย (KTB)",
        "ธนาคารกรุงเทพ (BBL)",
        "ธนาคารกรุงศรีอยุธยา (BAY)",
        "ธนาคารกสิกรไทย (KBANK)",
        "ธนาคารทิสโก้ (TISCO)",
        "ธนาคารเกียรตินาคินภัทร (KKP)",
        "ธนาคารซีไอเอ็มบี ไทย (CIMBT)",
        "ธนาคารทหารไทยธนชาต (TTB)",
        "ธนาคารไทยพาณิชย์ (SCB)",
        "ธนาคารยูโอบี (UOB)",
        "ธนาคารแลนด์ แอนด์ เฮ้าส์ (LH)",
        "ธนาคารสแตนดาร์ดชาร์เตอร์ด (ไทย) (SC)"
    )

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true, // ✅ ย้ายไปอยู่ในพารามิเตอร์ที่ถูกต้อง
            value = selectedBank.value,
            onValueChange = {},
            label = { Text("ธนาคาร") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            bankList.forEach { bank ->
                DropdownMenuItem(
                    text = { Text(bank) },
                    onClick = {
                        selectedBank.value = bank
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ImagePicker(label: String, imageUrl: String, onImageSelected: (Uri) -> Unit) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                onImageSelected(it)
            }
        }
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .clickable { imagePickerLauncher.launch("image/*") } // Open gallery when clicked
        ) {
            Image(
                painter = if (imageUrl.isNotEmpty() && imageUrl != "no image") {
                    rememberAsyncImagePainter(imageUrl)
                } else {
                    painterResource(id = R.drawable.image_search) // Default image
                },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 🔹 Text + Button (Right Side)
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f) // Allow text & button to expand
        ) {
            Text(label, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(0.7f) // Adjust button width
            ) {
                Text("เลือกไฟล์")
            }
        }
    }
}