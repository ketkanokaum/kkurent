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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow

//@OptIn(ExperimentalMaterial3Api::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBarHome(
    navController: NavController,
    searchText: String,
    onSearch: (String) -> Unit // เพิ่มพารามิเตอร์ฟังก์ชันนี้
) {
    val context = LocalContext.current
    val isUserRole = SharedPreferencesManager(context).role == "user"

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
                        text = if(isUserRole) "หน้าแรก | " else "หน้าแรก",
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
    val sharedPreferences = SharedPreferencesManager(context)

    var postList by remember { mutableStateOf(listOf<Post>()) }
    var searchTextForPost by remember { mutableStateOf("") } // สร้างตัวแปรสำหรับเก็บค่าคำค้นหา
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Post>("data") ?: Post(
        0,
        "",
        "",
        0,
        "",
        "",
        "",
        ""
    )
    var postId by remember { mutableIntStateOf(data.idPost) }
    val currentUserRole = sharedPreferences.role
    var softDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // ถ้ายังไม่ล็อคอินไล่ไป login ก่อน
        if(!sharedPreferences.isLoggedIn) {
            navController.navigate(Screen.Login.route)
        }
    }

    LaunchedEffect(searchTextForPost) {
        if (searchTextForPost.isEmpty()) {
            showAllDataPost(context) { newPostList ->
                postList = newPostList
            }
        } else {
            searchPosts(searchTextForPost, context) { newPostList ->
                postList = newPostList
            }
        }
    }

    if (softDeleteDialog) {
        AlertDialog(
            onDismissRequest = { softDeleteDialog = false },
            text = { Text("คุณต้องการลบโพสต์นี้ใช่หรือไม่") },
            confirmButton = {
                TextButton(
                    onClick = {
                        createClient.softDeletePost(postId).enqueue(object : Callback<Post> {
                            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "ลบโพสต์สำเร็จ!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    postList = postList.filter { it.idPost != postId }
                                    softDeleteDialog = false
                                    navController.popBackStack(Screen.Home.route, false)
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
            searchPosts(query, context) { newPostList ->
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
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = (135 + 16).dp).padding(horizontal = 16.dp)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "โพสต์ทั้งหมด",
                                fontSize = 20.sp,
                                color = Color(0xFF0D346D),
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                Icons.Default.People,
                                contentDescription = "AllProduct",
                                tint = Color(0xFF0D346D),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "${postList.size} รายการ",
                            color = Color.Gray,
                            fontSize = 16.sp,
                        )
                    }
                }

                // แสดงรายการโพสต์สินค้า
                items(postList) { item ->
                    val isCurrentUser = sharedPreferences.isCurrentUser(item.user_id)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(item.post_img),
                                contentDescription = item.post_detail,
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(item.profilePicture),
                                        contentDescription = item.post_detail,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    Text(
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        text = if (isCurrentUser) "โพสต์ของคุณ" else item.fname_lname,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                    if(isCurrentUser) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "my item",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(12.dp),
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = formatDateTime(item.createAt),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.post_detail,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                if (currentUserRole == "user") {
                                    if (isCurrentUser) {
                                        IconButton(
                                            onClick = {
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
                                                tint = Color(0xFF0D346D),
                                                modifier = Modifier.size(24.dp) // ปรับขนาดไอคอน
                                            )
                                        }
                                    } else {
                                        IconButton (
                                            onClick = {
                                                navController.currentBackStackEntry?.savedStateHandle?.set<Int>(
                                                    "chatOtherUserId",
                                                    item.user_id
                                                )
                                                navController.navigate(Screen.ChatDetail.route)
                                            },
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.Chat,
                                                contentDescription = "Chat",
                                                tint = Color(0xFF0D346D),
                                                modifier = Modifier.size(24.dp) // ปรับขนาดไอคอน
                                            )
                                        }
                                    }
                                }
                                if (isCurrentUser || currentUserRole == "admin") {
                                    IconButton(
                                        onClick = {
                                            postId = item.idPost // กำหนดค่า postId
                                            softDeleteDialog = true // เปิด Dialog
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

                item {
                    Spacer(modifier = Modifier.height(0.dp))
                }
            }
        }

        if (currentUserRole == "user") {
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
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
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