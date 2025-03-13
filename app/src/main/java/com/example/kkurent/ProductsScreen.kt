package com.example.kkurent

import android.content.Context
import android.graphics.Paint.Align
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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter


@Composable
fun ProductsScreen(navController: NavController) {
    val context = LocalContext.current
    val createClient = kkurentApi.create()
    var itemList by remember { mutableStateOf(listOf<Item>()) }
    var searchTextForItem by remember { mutableStateOf("") } // สร้างตัวแปรสำหรับเก็บค่าคำค้นหา
    val sharedPreferences = SharedPreferencesManager(context)

    var selectedCategory by remember { mutableStateOf("ทั้งหมด") }
    var selectedLocation by remember { mutableStateOf("ทั้งหมด") }

    val userRole = sharedPreferences.role
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Item>("data") ?: Item(
        0,
        "",
        "",
        0,
        "",
        "",
        "",
        "",
        "",
        0,
        "",
        "",
        ""
    )

    var items_id by remember { mutableStateOf(data?.idItems ?: 0) }
    var softDeleteDialog by remember { mutableStateOf(false) }
    LaunchedEffect(searchTextForItem) {
        val currentUserId = SharedPreferencesManager(context).idUsers
        if (searchTextForItem.isEmpty()) {
            showAllDataItem(context) { newItemList ->
                val myItems: List<Item> =
                    newItemList.filter { it.user_id == currentUserId } // กรองข้อมูลแค่ของตัวเอง
                val otherItems: List<Item> =
                    newItemList.filter { it.user_id != currentUserId } // กรองข้อมูลของคนอื่น
                itemList = myItems + otherItems // เอาข้อมูลของตัวเองมาต่อข้างหน้าข้อมูลของคนอื่น
            }
        } else {
            searchItem(searchTextForItem, context) { newItemList ->
                val myItems: List<Item> =
                    newItemList.filter { it.user_id == currentUserId } // กรองข้อมูลแค่ของตัวเอง
                val otherItems: List<Item> =
                    newItemList.filter { it.user_id != currentUserId } // กรองข้อมูลของคนอื่น
                itemList = myItems + otherItems // เอาข้อมูลของตัวเองมาต่อข้างหน้าข้อมูลของคนอื่น
            }
        }
    }

    if (softDeleteDialog) {
        AlertDialog(
            onDismissRequest = { softDeleteDialog = false },
            text = { Text("คุณต้องการลบสินค้าใช่หรือไม่") },
            confirmButton = {
                TextButton(
                    onClick = {
                        createClient.softDeleteItems(items_id).enqueue(object : Callback<Item> {
                            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "ลบสินค้าสำเร็จ!", Toast.LENGTH_SHORT)
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

// กรองรายการสินค้า
    val filteredItemList = itemList.filter { item ->
        (selectedCategory == "ทั้งหมด" || item.category_name == selectedCategory) &&
                (selectedLocation == "ทั้งหมด" || item.location == selectedLocation) &&
                (searchTextForItem.isEmpty() || item.item_name.contains(
                    searchTextForItem,
                    ignoreCase = true
                ))
    }

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
            modifier = Modifier
                .padding(top = (135 + 16).dp)
                .padding(horizontal = 16.dp)
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "สินค้าทั้งหมด",
                            fontSize = 20.sp,
                            color = Color(0xFF0D346D),
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "AllProduct",
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
                    ProductItem(item, {
                        items_id = item.idItems
                        softDeleteDialog = true
                    }, navController)
                }
            }
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.size(0.dp))
            }
        }
        if (userRole == "user") {
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

@Composable
fun ActionIcon(
    imageVector: ImageVector,
    contentDescription: String,
    tint: Color,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
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
        shape = RoundedCornerShape(size = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (productsList.option == selectedCategory) Color(0xFFF59949) else Color(
                0xFFF4F4F4
            )
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
            containerColor = if (positionList.option == selectedLocation) Color(0xFFF59949) else Color(
                0xFFF4F4F4
            )
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "หมวดหมู่สินค้า",
                fontSize = 20.sp,
                color = Color(0xFF0D346D),
                fontWeight = FontWeight.Bold,
            )
            Icon(
                Icons.Default.Star,
                contentDescription = "CategoryProduct",
                tint = Color(0xFF0D346D),
                modifier = Modifier.size(16.dp)
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(productsList) { item ->
                ItemProductsLayoutRow2(
                    productsList = item,
                    selectedCategory = selectedCategory,
                    onCategorySelected = onCategorySelected
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "สถานที่",
                fontSize = 20.sp,
                color = Color(0xFF0D346D),
                fontWeight = FontWeight.Bold,

                )
            Spacer(Modifier.width(8.dp))
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = Color(0xFF0D346D),
                modifier = Modifier.size(17.dp)
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
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
                        text = if(isUserRole) "สินค้าเช่า | " else "สินค้าเช่า",
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

@Composable
fun ProductItem(item: Item, onDeleteClick: () -> Unit, navController: NavController) {
    val updatedStatus =
        navController.currentBackStackEntry?.savedStateHandle?.get<String>("updatedStatus_${item.idItems}")
    val displayStatus = updatedStatus ?: item.item_status
    val normalizedStatus = when (displayStatus) {
        "available" -> "ว่าง"
        "unavailable" -> "ไม่ว่าง"
        else -> displayStatus
    }
    val context = LocalContext.current
    val sharedPreferences = SharedPreferencesManager(context)
    val userRole = sharedPreferences.role
    val isCurrentUser = sharedPreferences.isCurrentUser(item.user_id)
    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
        Box(
            modifier = Modifier.fillMaxSize() // ขยายให้เต็มขนาดพื้นที่
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 8.dp)
                    .zIndex(1f), horizontalArrangement = Arrangement.End
            ) {
                Box(Modifier.clip(RoundedCornerShape(8.dp))) {
                    Text(
                        text = if (normalizedStatus == "ว่าง") "ว่างให้เช่า" else "ถูกเช่าอยู่",
                        fontSize = 12.sp,
                        color = if (normalizedStatus == "ว่าง") Color(0xFF39B54A) else Color(
                            0xFFE84A3F
                        ), // เขียว/แดง
                        modifier = Modifier
                            .align(Alignment.TopEnd) // ชิดขวาบนสุด
                            .background(
                                if (normalizedStatus == "ว่าง") Color(0xFFEAF9E9) else Color(
                                    0xFFFFD5D5
                                )
                            ) // สีพื้นหลัง
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            ) // ขยายขนาดพื้นที่
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AsyncImage(
                    model = item.item_img,
                    contentDescription = item.item_name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
                Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(item.profilePicture),
                            contentDescription = item.item_name,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = if (isCurrentUser) "สินค้าของคุณ" else item.fname_lname,
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
                        text = formatDateTime(item.createAt),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = item.item_name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${item.price} บาท / วัน",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF59949)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically // จัดให้อยู่ตรงกลางแนวตั้ง
                    ) {
                        if (userRole == "user" && isCurrentUser) {
                            ActionIcon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Post",
                                tint = Color(0xFF0D346D)
                            ) {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "data",
                                    item
                                )
                                navController.navigate(Screen.EditItem.route)
                            }
                        }

                        if (userRole == "admin" || userRole == "user" && isCurrentUser) {
                            ActionIcon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete Icon",
                                tint = Color.Red
                            ) {
                                onDeleteClick()
                            }
                        }

                        if (userRole == "user" && !isCurrentUser) {
                            ActionIcon(
                                imageVector = Icons.AutoMirrored.Filled.Chat,
                                contentDescription = "Chat",
                                tint = Color(0xFF0D346D)
                            ) {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "chatOtherUserId",
                                    item.user_id
                                )
                                navController.navigate(Screen.ChatDetail.route)
                            }
                        }
                    }


                }
            }
        }
    }
}
