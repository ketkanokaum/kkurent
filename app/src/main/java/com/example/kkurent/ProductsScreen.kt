package com.example.kkurent

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.kkurent.SharedPreferencesManager.Companion.KEY_FNAME_LNAME


@Composable
fun ProductsScreen(navController: NavController) {

    val context = LocalContext.current
    val createClient = kkurentApi.create()
    var itemList by remember { mutableStateOf(listOf<Item>()) }
    var searchTextForItem by remember { mutableStateOf("") } // สร้างตัวแปรสำหรับเก็บค่าคำค้นหา
    val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    var selectedCategory by remember { mutableStateOf("ทั้งหมด") }
    var selectedLocation by remember { mutableStateOf("ทั้งหมด") }
    val currentUserFnameLname = sharedPreferences.getString("key_fname_lname", "")

    val userRole = sharedPreferences.getString("key_role", "")
    val lifecycleOwner = LocalLifecycleOwner.current
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Item>("data") ?: Item(0,"","",0,"","","","","",0)

    var items_id by remember { mutableStateOf(data?.idItems ?: 0) }
    var softDeleteDialog by remember {  mutableStateOf(false) }
    LaunchedEffect(searchTextForItem) {
        if (searchTextForItem.isEmpty()) {
            showAllDataItem(context) { newItemList ->
                itemList = newItemList
            }
        } else {
            searchItem(searchTextForItem, context) { newItemList ->
                itemList = newItemList
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
                        if (items_id == 0) {
                            Toast.makeText(context, "ไม่พบ ID Post", Toast.LENGTH_SHORT).show()
                            return@TextButton
                        }

                        createClient.softDeleteItems(items_id).enqueue(object : Callback<Item> {
                            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "ลบโพสต์สำเร็จ!", Toast.LENGTH_SHORT).show()
                                    itemList = itemList.filter { it.idItems != items_id }
                                    softDeleteDialog = false
                                    navController.popBackStack(Screen.Products.route, false)
                                } else {
                                    Toast.makeText(context, "ลบโพสต์ไม่สำเร็จ!", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Item>, t: Throwable) {
                                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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


// กรองรายการสินค้า
    val filteredItemList = itemList.filter { item ->
        (selectedCategory == "ทั้งหมด" || item.category_name == selectedCategory) &&
                (selectedLocation == "ทั้งหมด" || item.location == selectedLocation) &&
                (searchTextForItem.isEmpty() || item.item_name.contains(searchTextForItem, ignoreCase = true))
    }

//        LaunchedEffect(lifecycleState) {
//            if (lifecycleState == Lifecycle.State.RESUMED) {
//                showAllData(itemList, contextForToast) { newItemList ->
//                    itemList = newItemList
//                }
//            }
//        }

        //ทั้งหน้า
        Box(modifier = Modifier.fillMaxSize()) {
            MyTopAppBarProducts(navController, searchTextForItem) { query ->
                searchTextForItem = query // อัปเดตค่า searchText เมื่อมีการพิมพ์ในช่องค้นหา
                searchItem(query, context) { newItemList ->
                    itemList = newItemList
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // มี 2 คอลัมน์
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 150.dp, start = 16.dp, end = 16.dp)
            ) {

                // หมวดหมู่สินค้า
                item(span = { GridItemSpan(2) }) {
                    CatagoryProductsLazyRow(
                        selectedCategory = selectedCategory,
                        selectedLocation = selectedLocation,
                        onCategorySelected = { selectedCategory = it },
                        onLocationSelected = { selectedLocation = it }
                    )
                }

                item(span = { GridItemSpan(2) }) {
                    Row {
                        Text(
                            text = "สินค้าทั้งหมด",
                            fontSize = 20.sp,
                            color = Color(0xFF0D346D),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "AllProduct",
                            tint = Color(0xFF0D346D),
                            modifier = Modifier.padding(start = 10.dp, top = 7.dp).size(17.dp)
                        )
                    }
                }

                // ตรวจสอบว่า filteredItemList ว่างหรือไม่
                if (filteredItemList.isEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 50.dp),
                                text = "ไม่มีสินค้าที่ค้นหา",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    // แสดงรายการโพสต์สินค้า
                    items(filteredItemList) { item ->
                        val updatedStatus =
                            navController.currentBackStackEntry?.savedStateHandle?.get<String>("updatedStatus_${item.idItems}")
                        val displayStatus = updatedStatus ?: item.item_status
                        val normalizedStatus = when (displayStatus) {
                            "available" -> "ว่าง"
                            "unavailable" -> "ไม่ว่าง"
                            else -> displayStatus
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(1.dp)
                                .height(250.dp),
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
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                            ) {

                                val imageUrl = item.item_img
                                if (!imageUrl.isNullOrBlank()) {

                                    AsyncImage(
                                        model = imageUrl,
                                        contentDescription = "Item Image",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                } else {
                                    Text(text = " ", fontSize = 16.sp)
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.item_name,
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "ราคา ${item.price} /วัน",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF59949)
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))

                                    // สถานะสินค้า
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .offset(y = -160.dp, x = 77.dp)
                                            .padding(4.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    ) {
                                        Text(
                                            text = if (normalizedStatus == "ว่าง") "ว่างให้เช่า" else "ถูกเช่าอยู่",
                                            fontSize = 12.sp,
                                            color = if (normalizedStatus == "ว่าง") Color(0xFF39B54A) else Color(0xFFE84A3F), // เปลี่ยนเป็นสีแดงเมื่อเป็น "ถูกเช่าอยู่"
                                            modifier = Modifier
                                                .background(if (normalizedStatus == "ว่าง") Color.White else Color(0xFFFFD5D5)) // สีพื้นหลังที่แตกต่างเมื่อ "ถูกเช่าอยู่"
                                                .padding(horizontal = 4.dp)
                                        )
                                    }
                                    if (userRole != "admin") {
                                        if(!SharedPreferencesManager(context).isCurrentUser(item.user_id)) {
                                            IconButton(
                                                onClick = {
                                                    navController.currentBackStackEntry?.savedStateHandle?.set<Int>(
                                                        "chatOtherUserId",
                                                        item.user_id
                                                    )
                                                    navController.navigate(Screen.ChatDetail.route)
                                                },
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.MailOutline,
                                                    contentDescription = "Chat Icon",
                                                    tint = Color(0xFF0D346D),
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        } else {
                                            IconButton(
                                                onClick = {
                                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                                        "data",
                                                        Item(
                                                            idItems = item.idItems,
                                                            item_name = item.item_name,
                                                            item_detail = item.item_detail,
                                                            price = item.price,
                                                            item_img = item.item_img,
                                                            location = item.location,
                                                            category_name = item.category_name,
                                                            fname_lname =item.fname_lname,
                                                            item_status = displayStatus,
                                                            user_id = item.user_id
                                                        )
                                                    )
                                                    navController.navigate(Screen.EditItem.route)
                                                },
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Edit Icon",
                                                    tint = Color(0xFF0D346D),
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }
                                    }

                                    if (userRole == "admin") {
                                        IconButton(
                                            onClick = {
                                                if (item.idItems != null && item.idItems != 0) {
                                                    items_id = item.idItems // กำหนดค่า postId
                                                    softDeleteDialog = true // เปิด Dialog
                                                } else {
                                                    Toast.makeText(context, "Invalid Items ID", Toast.LENGTH_SHORT).show()

                                                }


                                            },
                                            modifier = Modifier.size(24.dp)  // กำหนดขนาดให้ชัดเจน
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Delete,
                                                contentDescription = "Delete Icon",
                                                tint = Color.Red,
                                                modifier = Modifier.size(24.dp) // กำหนดขนาดของ icon
                                            )
                                        }

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
                    onClick = { navController.navigate(Screen.Item.route) }, // ไปหน้าเพิ่ม
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




// add items to the listหมวดหมู่สินค้า
data class ProductsList2(val option: String)
private fun prepareProductsList(): MutableList<ProductsList2> {
    val productsList = mutableListOf<ProductsList2>()
    productsList.add(ProductsList2(option = "ทั้งหมด"))
    productsList.add(ProductsList2(option = "เครื่องแต่งกาย"))
    productsList.add(ProductsList2(option = "อุปกรณ์อิเล็กทรอนิกส์"))
    productsList.add(ProductsList2(option = "หนังสือ"))
    productsList.add(ProductsList2(option = "กีฬา"))
    return productsList
}

// add items to the listหมวดหมู่สถานที่
data class PositionsList2(val option: String)
private fun preparePositionsList(): MutableList<PositionsList2> {
    val positionsList = mutableListOf<PositionsList2>()
    positionsList.add(PositionsList2(option = "ทั้งหมด"))
    positionsList.add(PositionsList2(option = "หลังมอ"))
    positionsList.add(PositionsList2(option = "กังสดาล"))
    positionsList.add(PositionsList2(option = "โคลัมโบ"))
    return positionsList
}
@Composable
private fun ItemProductsLayoutRow2(
    productsList: ProductsList2,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(size = 15.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (productsList.option == selectedCategory) Color(0xFFF59949) else Color(0xFFF4F4F4)
        ),
        modifier = Modifier.clickable { onCategorySelected(productsList.option) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 18.dp)
        ) {
            Text(
                text = productsList.option,
                color = if (productsList.option == selectedCategory) Color.White else Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun ItemPositionsLayoutRow2(
    positionList: PositionsList2,
    selectedLocation: String,
    onLocationSelected: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(size = 15.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (positionList.option == selectedLocation) Color(0xFFF59949) else Color(0xFFF4F4F4)
        ),
        modifier = Modifier.clickable { onLocationSelected(positionList.option) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 18.dp)
        ) {
            Text(
                text = positionList.option,
                color = if (positionList.option == selectedLocation) Color.White else Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun CatagoryProductsLazyRow(
    selectedCategory: String,
    selectedLocation: String,
    onCategorySelected: (String) -> Unit,
    onLocationSelected: (String) -> Unit
) {
    val productsList = prepareProductsList()
    val positionList = preparePositionsList()

    Column {
        Row {
            Text(
                text = "หมวดหมู่สินค้า",
                fontSize = 20.sp,
                color = Color(0xFF0D346D),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
            Icon(
                Icons.Default.Star,
                contentDescription = "CategoryProduct",
                tint = Color(0xFF0D346D),
                modifier = Modifier.padding(start = 10.dp, top = 7.dp).size(17.dp)
            )
        }

        LazyRow(
            modifier = Modifier.padding(start = 1.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)) {
            items(productsList) { item ->
                ItemProductsLayoutRow2(
                    productsList = item,
                    selectedCategory = selectedCategory,
                    onCategorySelected = onCategorySelected
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(
                text = "สถานที่",
                fontSize = 20.sp,
                color = Color(0xFF0D346D),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = Color(0xFF0D346D),
                modifier = Modifier.padding(start = 10.dp, top = 7.dp).size(17.dp)
            )
        }

        LazyRow(
            modifier = Modifier.padding(start = 1.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)) {
            items(positionList) { item ->
                ItemPositionsLayoutRow2(
                    positionList = item,
                    selectedLocation = selectedLocation,
                    onLocationSelected = onLocationSelected
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBarProducts(
    navController: NavController,
    searchTextForItem: String,
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
                        text = "สินค้าเช่า  | ",
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
                value = searchTextForItem, // ใช้ค่าจากตัวแปร
                onValueChange = { onSearch(it) }, // ส่งค่ากลับไปยัง HomeScreen เมื่อพิมพ์
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch(searchTextForItem) } // ใช้ฟังก์ชันที่ส่งมา
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

//ฟังก์ชันสำหรับค้นหาโพสต์
fun searchItem(query: String, context: Context, onDataReceived: (List<Item>) -> Unit) {
    val createClient = kkurentApi.create()

    createClient.searchItems(query).enqueue(object : Callback<List<Item>> {
        override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                onDataReceived(posts)
            } else {
//                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<List<Item>>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

fun showAllDataItem(context: Context, onDataReceived: (List<Item>) -> Unit) {
//fun showAllData(itemList: List<Item>, context: Context, onDataReceived: (List<Item>) -> Unit) {
    val createClient = kkurentApi.create()

    createClient.retrieveItems().enqueue(object : Callback<List<Item>> {
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




