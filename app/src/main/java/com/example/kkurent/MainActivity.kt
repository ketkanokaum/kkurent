package com.example.kkurent

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets.Type.statusBars
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kkurent.ui.theme.KKUrentTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KKUrentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyScreen()
                }

            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavHostController) {

    var expanded by remember { mutableStateOf(false) }

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
                NotificationButton(navController)
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

            var searchText by remember { mutableStateOf("") } // สร้างตัวแปรเก็บค่าข้อความ

            TextField(
                value = searchText, // ใช้ค่าจากตัวแปร
                onValueChange = { searchText = it }, //ไว้ค้นหาและอัปเดตค่าตัวแปรเมื่อพิมพ์
                keyboardActions = KeyboardActions(
                    onSearch = {
                        // ค้นหาทันทีเมื่อกด Enter หรือปุ่มค้นหา
                    }
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
fun MyBottomBarAdmin(navController: NavHostController) {
    val navigationItems = listOf(
        Screen.Home,
        Screen.Products,
        Screen.Restore,
        Screen.AdminApproval,
        Screen.Profile,
    )
    var selectedScreen by remember { mutableStateOf(navController.previousBackStackEntry?.savedStateHandle?.get<Int>("selectedScreen") ?: 0) }

    NavigationBar(
        containerColor = Color(0xFFF4F4F4), // สีพื้นหลัง
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .shadow(8.dp, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        navigationItems.forEachIndexed { index, screen ->
            val selectedColor = if (selectedScreen == index) Color(0xFFF59949) else Color.Gray

            NavigationBarItem(
                icon = { screen.icon?.let { Icon(imageVector = it, contentDescription = null, tint = selectedColor) } },
                label = { Text(text = screen.name, color = selectedColor) },
                selected = (selectedScreen == index),
                onClick = {
                    selectedScreen = index
                    navController.currentBackStackEntry?.savedStateHandle?.set<Int>(
                        "selectedScreen",
                        selectedScreen
                    )
                    navController.navigate(screen.route)
                }
            )
        }
    }
}

@Composable
fun MyBottomBar(navController: NavHostController) {
    val navigationItems = listOf(
        Screen.Home,
        Screen.Products,
        Screen.Chat,
        Screen.Favorites,
        Screen.Profile,
    )
    var selectedScreen by remember { mutableStateOf(navController.previousBackStackEntry?.savedStateHandle?.get<Int>("selectedScreen") ?: 0) }

    NavigationBar(
        containerColor = Color(0xFFF4F4F4), // สีพื้นหลัง
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .shadow(8.dp, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        navigationItems.forEachIndexed { index, screen ->
            val selectedColor = if (selectedScreen == index) Color(0xFFF59949) else Color.Gray

            NavigationBarItem(
                icon = { screen.icon?.let { Icon(imageVector = it, contentDescription = null, tint = selectedColor) } },
                label = { Text(text = screen.name, color = selectedColor) },
                selected = (selectedScreen == index),
                onClick = {
                    selectedScreen = index
                    navController.currentBackStackEntry?.savedStateHandle?.set<Int>(
                        "selectedScreen",
                        selectedScreen
                    )
                    navController.navigate(screen.route)
                }
            )
        }
    }
}


@Composable
fun MyScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    // ดึงเส้นทาง (Route) ปัจจุบัน
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val userRole = sharedPreferences.getString("key_role", "")
    Scaffold(
        bottomBar = {
            if (userRole == "admin") {
                // หาก role เป็น admin ใช้ bottomBar ของ admin
                MyBottomBarAdmin(navController)
            } else if (currentRoute == Screen.Home.route ||
                currentRoute == Screen.Products.route ||
                currentRoute == Screen.Favorites.route ||
                currentRoute == Screen.Chat.route ||
                currentRoute == Screen.Profile.route) {
                // ถ้าไม่ใช่ admin แสดง bottomBar สำหรับผู้ใช้งานทั่วไป
                MyBottomBar(navController)
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavGraph(navController = navController)
        }
    }
}








//
//@Composable
//fun MyScreen() {
//
//    val navController = rememberNavController()
//    Column(
//        modifier = Modifier
//            .fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        NavGraph(navController = navController)
//    }
//}
//
