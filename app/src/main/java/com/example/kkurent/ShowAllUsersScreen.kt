package com.example.kkurent

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.kkurent.SharedPreferencesManager.Companion.KEY_FNAME_LNAME

//@OptIn(ExperimentalMaterial3Api::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBarShowUsers(
    navController: NavController,
    searchText: String,
    onSearch: (String) -> Unit // เพิ่มพารามิเตอร์ฟังก์ชันนี้
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
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
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
                placeholder = { Text("ค้นหาผู้ใช้งาน", color = Color.Gray) },
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
fun ShowAllUsersScreen(navController: NavController) {
    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    val loggedInUserFnameLname = sharedPreferences.getString(KEY_FNAME_LNAME, null)  // ดึงชื่อผู้ใช้จาก sharedPreferences
    val contextForToast = LocalContext.current
    var userList by remember { mutableStateOf(listOf<Register>()) }
    var searchTextForUser by remember { mutableStateOf("") } // สร้างตัวแปรสำหรับเก็บค่าคำค้นหา
    val currentUserFnameLname = sharedPreferences.getString("key_fname_lname", "")


// เมื่อค่าของ searchText เปลี่ยนแปลง ให้เรียกค้นหาตามคำค้นหา
    LaunchedEffect(searchTextForUser) {
        if (searchTextForUser.isEmpty()) {
            showAllDataUsers(contextForToast) { newUserList ->
                userList = newUserList
            }
        } else {
            searchUsers(searchTextForUser, contextForToast) { newUserList ->
                userList = newUserList
            }
        }
    }

    //ทั้งหน้า
    Box(modifier = Modifier.fillMaxSize()) {
        MyTopAppBarShowUsers(navController, searchTextForUser) { query ->
            searchTextForUser = query // อัปเดตค่า searchText เมื่อมีการพิมพ์ในช่องค้นหา
            searchUsers(query, contextForToast) { newUserList ->
                userList = newUserList
            }
        }

        if (userList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ไม่มีผู้ใช้ที่ค้นหา",
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "ผู้ใช้ทั้งหมด",
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
                            text = "${userList.size} รายการ",
                            color = Color.Gray,
                            fontSize = 16.sp,
                        )
                    }
                }

                val UserListNotAdmin = userList.filter { it.role != "admin" }

                // แสดงรายการโพสต์สินค้า
                items(UserListNotAdmin) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "data",
                                Register(
                                    item.idUsers,
                                    item.fname_lname,
                                    item.email,
                                    item.address,
                                    item.bank,
                                    item.account_number,
                                    item.account_name,
                                    item.password,
                                    item.phone,
                                    item.profile_picture,
                                    item.id_card_img,
                                    item.role,
                                    item.del_status,
                                    item.createAt,
                                    item.deleteAt
                                )
                            )
                            navController.navigate(Screen.DetailUser.route)
                        }
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
                                modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop,
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.fname_lname,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Text(
                                        text = "${item.email}",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                }
                                Text(
                                    text = "สร้างเมื่อ: " + formatDateTime(item.createAt),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(Modifier.width(0.dp))
                }
            }
        }
    }
}



// ฟังก์ชันสำหรับค้นหาusers
fun searchUsers(query: String, context: Context, onDataReceived: (List<Register>) -> Unit) {
    val createClient = userApi.create()

    createClient.searchUsers(query).enqueue(object : Callback<List<Register>> {
        override fun onResponse(call: Call<List<Register>>, response: Response<List<Register>>) {
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                onDataReceived(posts)
            } else {
//                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<List<Register>>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

// ฟังก์ชันแสดงusersทั้งหมด
fun showAllDataUsers(context: Context, onDataReceived: (List<Register>) -> Unit) {
    val createClient = userApi.create()

    createClient.showAllUsers().enqueue(object : Callback<List<Register>> {
        override fun onResponse(call: Call<List<Register>>, response: Response<List<Register>>) {
            if (response.isSuccessful) {
                val users = response.body()?.filter { it.del_status != "Y" } ?: emptyList()
                onDataReceived(users)
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        override fun onFailure(call: Call<List<Register>>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}


