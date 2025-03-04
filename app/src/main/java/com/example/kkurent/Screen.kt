package com.example.kkurent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen (val route: String, val name: String,val icon: ImageVector? = null){
    data object Login : Screen(route="login_screen",name="Login")
    data object Register : Screen(route="register_screen",name="Register")
    data object Password : Screen(route="password_screen",name="Password")
    data object Post: Screen(route = "post_screen", name = "Post")
    data object Item: Screen(route = "item_screen", name = "Item")
    data object Notification: Screen(route = "notification_screen", name = "Notification")
    data object Products : Screen(route = "products_screen", name = "สินค้าเช่า", icon = Icons.Default.ShoppingCart)
    data object Favorites : Screen(route = "Favorites_screen", name = "สินค้าโปรด", icon = Icons.Default.Favorite)
    data object Profile : Screen(route = "profile_screen", name = "โปรไฟล์", icon = Icons.Default.Face)
    data object ReviewList : Screen("review_list", "All Reviews")
    data object InsertReview : Screen("insert_review", "Add Review")
    data object Home: Screen(route = "Home_screen", name = "หน้าแรก",icon = Icons.Default.Home)
    data object EditPost: Screen(route = "EditPost_screen", name = "EditPost")
    data object EditItem: Screen(route = "EditItem_screen", name = "EditItem")
    data object Restore: Screen(route = "RestoreScreen_screen",  name = "กู้คืน",icon = Icons.Default.Autorenew)
    data object RegistrationRequests : Screen("registration_requests", "Registration Requests", null)
    data object Registration: Screen(route = "Registration_screen", name = "ลงทะเบียน",icon = Icons.Default.PeopleAlt)
    data object AdminApproval : Screen("AdminApproval_screen", "คำขออนุมัติ", icon = Icons.Default.Feedback)
    data object Detail: Screen(route = "detail/{idItems}", name = "Detail")
    data object DetailUser: Screen(route = "DetailUser_screen", name = "DetailUser")
    data object ShowAllUsers: Screen(route = "ShowAllUsers_screen", name = "ShowAllUsers")
    data object EditProfile: Screen(route = "EditProfile_screen", name = "Edit Profile")
    data object Chat : Screen(route = "chat_screen", name = "แชท", icon = Icons.Default.Email)
    data object ChatDetail : Screen(route = "chat_detail_screen", name = "แชท", icon = Icons.Default.Email)
}