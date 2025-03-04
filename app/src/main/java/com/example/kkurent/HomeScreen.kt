package com.example.kkurent

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.kkurent.Post
import com.example.kkurent.R
import com.example.kkurent.Screen
import com.example.kkurent.kkurentApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.kkurent.SharedPreferencesManager.Companion.KEY_FNAME_LNAME
import com.example.kkurent.SharedPreferencesManager.Companion.KEY_ROLE
import com.example.kkurent.SharedPreferencesManager.Companion.KEY_USER_ID
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone


//@OptIn(ExperimentalMaterial3Api::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBarHome(
    navController: NavController,
    searchText: String,
    onSearch: (String) -> Unit // เพิ่มพารามิเตอร์ฟังก์ชันนี้
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
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "หน้าแรก  | ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFA655), // สีส้ม
                        modifier = Modifier.padding(top = 5.dp)
                    )
                    NotificationButton(navController)
                }
            }
        )

        // Search Bar ด้านล่างของ TopAppBar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-20).dp), // ดึงลงมาให้คร่อม top bar
            contentAlignment = Alignment.Center
        ) {
            TextField(
                value = searchText, // ใช้ค่าจากตัวแปร
                onValueChange = { onSearch(it) }, // ส่งค่ากลับไปยัง HomeScreen เมื่อพิมพ์
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch(searchText) } // ใช้ฟังก์ชันที่ส่งมา
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,  // ใช้คีย์บอร์ดทั่วไป
                    imeAction = ImeAction.Search       // เปลี่ยนปุ่ม Enter เป็นปุ่มค้นหา
                ),
                placeholder = { Text("ค้นหา", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFFFFA655)
                    )
                },
                shape = RoundedCornerShape(50.dp), // ทำให้เป็นมุมโค้งมน
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF4F4F4), // สีพื้นหลัง
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
    }
}


@Composable
fun HomeScreen(navController: NavController) {
    val createClient = kkurentApi.create()
    val context = LocalContext.current
    val initialUser = Register(0, "", "", "", "", "", "", "", "", "", "", "", "") // ข้อมูลเริ่มต้น
    var userItems by remember { mutableStateOf(initialUser) }
    val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    val loggedInUserFnameLname = sharedPreferences.getString(KEY_FNAME_LNAME, null)
    val userId =
        sharedPreferences.getString("userId", "0")!!.toInt() // ดึงชื่อผู้ใช้จาก sharedPreferences
    val contextForToast = LocalContext.current
    var postList by remember { mutableStateOf(listOf<Post>()) }
    var searchTextForPost by remember { mutableStateOf("") } // สร้างตัวแปรสำหรับเก็บค่าคำค้นหา
    val currentUserFnameLname = sharedPreferences.getString("key_fname_lname", "")
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Post>("data") ?: Post(
        0,
        "",
        "",
        0,
        ""
    )
    var post_id by remember { mutableStateOf(data?.idPost ?: 0) }

    val userRole = sharedPreferences.getString("key_role", "")
    val sellerId = userId // ใช้ ID ของผู้ขายจากโพสต์
    var softDeleteDialog by remember { mutableStateOf(false) }


    LaunchedEffect(searchTextForPost) {
        if (searchTextForPost.isEmpty()) {
            showAllDataPost(contextForToast) { newPostList ->
                postList = newPostList
            }
        } else {
            searchPosts(searchTextForPost, contextForToast) { newPostList ->
                postList = newPostList
            }
        }
    }


    if (softDeleteDialog) {
        AlertDialog(
            onDismissRequest = { softDeleteDialog = false },
//            title = { Text("Warning") },
            text = { Text("คุณต้องการลบโพสต์นี้ใช่หรือไม่") },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (post_id == 0) {
                            Toast.makeText(contextForToast, "ไม่พบ ID Post", Toast.LENGTH_SHORT)
                                .show()
                            return@TextButton
                        }

                        createClient.softDeletePost(post_id).enqueue(object : Callback<Post> {
                            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        contextForToast,
                                        "ลบโพสต์สำเร็จ!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    postList = postList.filter { it.idPost != post_id }
                                    softDeleteDialog = false
                                    navController.popBackStack(Screen.Home.route, false)
                                } else {
                                    Toast.makeText(
                                        contextForToast,
                                        "ลบโพสต์ไม่สำเร็จ!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<Post>, t: Throwable) {
                                Toast.makeText(
                                    contextForToast,
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
                TextButton(onClick = { softDeleteDialog = false }) {
                    Text("ไม่")
                }
            }
        )
    }


    //ทั้งหน้า
    Box(modifier = Modifier.fillMaxSize()) {
        MyTopAppBarHome(navController, searchTextForPost) { query ->
            searchTextForPost = query // อัปเดตค่า searchText เมื่อมีการพิมพ์ในช่องค้นหา
            searchPosts(query, contextForToast) { newPostList ->
                postList = newPostList
            }
        }

        if (postList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ไม่มีโพสต์ที่ค้นหา",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(top = 148.dp)
            ) {

                item {
                    Row(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = "โพสต์ทั้งหมด",
                            fontSize = 20.sp,
                            color = Color(0xFF0D346D),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        Icon(
                            Icons.Default.Groups,
                            contentDescription = "Allpost",
                            tint = Color(0xFF0D346D),
                            modifier = Modifier.padding(start = 10.dp, top = 7.dp).size(17.dp)
                        )
                    }
                }

                // แสดงรายการโพสต์สินค้า
                items(postList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(150.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
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

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    if (item.fname_lname == currentUserFnameLname) {
                                        Text(
                                            text = "โพสต์โดยคุณ",
                                            fontSize = 14.sp,
                                            color = Color.Gray
                                        )
                                    } else {
                                        Text(
                                            text = "โพสต์โดย: ${item.fname_lname}",
                                            fontSize = 14.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                                Text(
                                    text = item.post_detail,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                            // แสดงปุ่มแก้ไขเฉพาะเมื่อไม่ใช่ admin
                            if (userRole != "admin") {
                                if (SharedPreferencesManager(context).isCurrentUser(item.user_id)) {
                                    IconButton(onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "data",
                                            Post(
                                                item.idPost,
                                                item.post_detail,
                                                item.post_img,
                                                item.user_id,
                                                item.fname_lname
                                            )
                                        )
                                        navController.navigate(Screen.EditPost.route)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Post",
                                            tint = Color(0xFF0D346D),
                                            modifier = Modifier.size(24.dp) // ปรับขนาดไอคอน
                                        )
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            navController.currentBackStackEntry?.savedStateHandle?.set<Int>(
                                                "chatOtherUserId",
                                                item.user_id
                                            )
                                            navController.navigate(Screen.ChatDetail.route)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFFFFFFF)
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Mail,
                                            contentDescription = "Chat",
                                            tint = Color(0xFF0D346D),
                                            modifier = Modifier.size(24.dp) // ปรับขนาดไอคอน
                                        )
                                    }
                                }
                            } else {
                                // ปุ่มลบเฉพาะ admin
                                IconButton(
                                    onClick = {
                                        if (item.idPost != null && item.idPost != 0) {
                                            post_id = item.idPost // กำหนดค่า postId
                                            softDeleteDialog = true // เปิด Dialog
                                        } else {
                                            Toast.makeText(
                                                contextForToast,
                                                "Invalid Post ID",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            Log.d(
                                                "Debug",
                                                "item.idPost is null or 0: ${item.idPost}"
                                            )
                                        }

                                    },
                                    modifier = Modifier.size(24.dp)
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
        if (userRole != "admin") {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 16.dp),
                    onClick = {

                        navController.navigate(Screen.Post.route)
                    }, // ไปหน้าเพิ่ม
                    containerColor = Color(0xFFF59949),
                    shape = CircleShape // ทำให้เป็นทรงกลม
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add icon",
                        tint = Color.White
                    )
                }
            }
        }
    }
}


// ฟังก์ชันสำหรับค้นหาโพสต์
fun searchPosts(query: String, context: Context, onDataReceived: (List<Post>) -> Unit) {
    val createClient = kkurentApi.create()

    createClient.searchPost(query).enqueue(object : Callback<List<Post>> {
        override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                onDataReceived(posts)
            } else {
//                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<List<Post>>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

// ฟังก์ชันแสดงโพสต์ทั้งหมด
fun showAllDataPost(context: Context, onDataReceived: (List<Post>) -> Unit) {
    val createClient = kkurentApi.create()

    createClient.retrievePost().enqueue(object : Callback<List<Post>> {
        override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                onDataReceived(posts)
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        override fun onFailure(call: Call<List<Post>>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

// add items to the listหมวดหมู่สินค้า
data class CategoryList(val option: String)

private fun prepareProductsList(): MutableList<CategoryList> {
    val categoryList = mutableListOf<CategoryList>()
    categoryList.add(CategoryList(option = "ทั้งหมด"))
    categoryList.add(CategoryList(option = "เครื่องแต่งกาย"))
    categoryList.add(CategoryList(option = "ของใช้ทั่วไป"))
    categoryList.add(CategoryList(option = "อิเล็กทรอนิกส์"))
    categoryList.add(CategoryList(option = "หนังสือ"))
    categoryList.add(CategoryList(option = "กีฬา"))
    return categoryList
}

@Composable
private fun ItemProductsLayoutRow(
    categoryList: CategoryList,
    context: Context = LocalContext.current.applicationContext
) {
    Card(
        shape = RoundedCornerShape(size = 25.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { } //เงื่อนไขตอนกด
                .padding(all = 8.dp)
        ) {
            Text(
                text = categoryList.option,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(start = 10.dp, end = 10.dp)
            )
        }
    }
}

// add items to the listหมวดหมู่สถานที่
data class PositionsList(val option: String)

private fun preparePositionsList(): MutableList<PositionsList> {
    val positionsList = mutableListOf<PositionsList>()
    positionsList.add(PositionsList(option = "ทั้งหมด"))
    positionsList.add(PositionsList(option = "หลังมอ"))
    positionsList.add(PositionsList(option = "กังสดาล"))
    positionsList.add(PositionsList(option = "โคลัมโบ"))
    positionsList.add(PositionsList(option = "โนนม่วง"))
    return positionsList
}

@Composable
private fun ItemPositionsLayoutRow(
    positionList: PositionsList,
    context: Context = LocalContext.current.applicationContext
) {
    Card(
        shape = RoundedCornerShape(size = 25.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { } //เงื่อนไขตอนกด
                .padding(all = 8.dp)
        ) {
            Text(
                text = positionList.option,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(start = 10.dp, end = 10.dp)
            )
        }
    }
}

@Composable
fun CatagoryHomeLazyRow() {
    val productsList = prepareProductsList()
    val positionList = preparePositionsList()
    Column {
        Row {
            Text(
                text = "หมวดหมู่สินค้า",
                fontSize = 20.sp,
                color = Color(0xFF0D346D),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp)
            )
            Icon(
                Icons.Default.Star,
                contentDescription = "CatagoryProduct",
                tint = Color(0xFF0D346D),
                modifier = Modifier
                    .padding(start = 10.dp, top = 7.dp)
                    .size(17.dp)
            )
        }
        LazyRow(
            modifier = Modifier.padding(start = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
        ) {
            items(productsList) { item ->
                ItemProductsLayoutRow(categoryList = item)
            }
        }
        Spacer(modifier = Modifier.height(height = 1.dp))
        Row {
            Text(
                text = "สถานที่",
                fontSize = 20.sp,
                color = Color(0xFF0D346D),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp)
            )
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = Color(0xFF0D346D),
                modifier = Modifier
                    .padding(start = 10.dp, top = 7.dp)
                    .size(17.dp)
            )
        }
        LazyRow(
            modifier = Modifier.padding(start = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
        ) {
            items(positionList) { item ->
                ItemPositionsLayoutRow(positionList = item)
            }
        }
    }
}


