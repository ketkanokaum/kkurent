package com.example.kkurent

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
//import com.example.kkurent.ChatScreen
//import com.example.kkurent.RegisterScreen
//import com.example.kkurent.RegisterScreen1
//import com.example.kkurent.RegisterScreen2
//import com.example.kkurent.RegisterScreen3
//import com.example.kkurent.ui.RegistrationDetailScreen
import com.example.kkurent.ui.RegistrationRequestsScreen

@Composable

fun NavGraph(navController: NavHostController) {

    // สร้างข้อมูลสมมุติสำหรับ userProfile และ posts
//    val userProfile = UserProfile(
//        username = "JohnDoe",
//        email = "johndoe@example.com",
//        address = "123 Main St, Khon Kaen",
//        phone = "0912345678",
//        bankAccount = "123-456-7890"
//    )

//    val posts = mutableListOf(
//        com.example.aroundkkurent.Post(
//            title = "My first post",
//            description = "This is the content of my first post."
//        ),
//        com.example.aroundkkurent.Post(
//            title = "My second post",
//            description = "This is the content of my second post."
//        ),
//        Post(title = "Another post", description = "Content of another post.")
//    )

    // สร้างข้อมูล reviews สำหรับแสดงในหน้าจอ
    val reviews = remember {
        mutableStateListOf(
            Review(1, "Great service!", 101, 201, 301),
            Review(2, "Could be better", 102, 202, 302),
            Review(3, "Highly recommended!", 103, 203, 303),
            Review(4, "Not worth the price", 104, 204, 304)
        )
    }
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {


        composable(
            route = Screen.Post.route
        ) {
            PostScreen(navController)
        }

        composable(
            route = Screen.Item.route
        ) {
            ItemScreen(navController)
        }

        composable(
            route = Screen.Notification.route
        ) {
            NotificationScreen(navController)
        }

        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(navController)
        }

        composable(
            route = Screen.Products.route
        ) {
            ProductsScreen(navController)
        }

        composable(
            route = Screen.EditPost.route
        ) {
            EditPostScreen(navController)
        }

        composable(
            Screen.ReviewList.route
        ) {
            ReviewListScreen(navController, reviews)
        }

        composable(
            Screen.InsertReview.route
        ) {
            InsertReviewScreen(navController, reviews)
        }

        composable(
            Screen.Profile.route
        ) {
            ProfileScreen(navController)
        }

        composable(
            route = Screen.EditItem.route
        ) {
            EditItemScreen(navController)
        }

        composable(
            route = Screen.Favorites.route
        ) {
            FavoritesScreen(navController)
        }

        composable(
            route = Screen.Restore.route
        ) {
            RestoreScreen(navController)
        }

        composable(
            route = Screen.Login.route
        ) {
            LoginScreen(navController)
        }

        composable(
            route = Screen.Register.route
        ) {
            RegisterScreen(navController)
        }

        composable(
            route = Screen.Password.route
        ) {
            PasswordScreen(navController)
        }
        composable(route = Screen.RegistrationRequests.route)
        {
            RegistrationRequestsScreen(navController)
        }

        composable(route = Screen.EditProfile.route)
        {
            EditProfileScreen(navController)
        }

        composable(route = Screen.AdminApproval.route)
        {
            AdminApproval(navController)
        }

        composable(route = Screen.DetailUser.route)
        {
            DetailUserScreen(navController)
        }


        composable(route = Screen.ShowAllUsers.route)
        {
            ShowAllUsersScreen(navController)
        }

        composable(Screen.Chat.route) { ChatScreen(navController) }
        composable(Screen.ChatDetail.route) { ChatDetailScreen(navController) }
        
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("idItems") { type = NavType.StringType })
        ) { backStackEntry ->
            val idItems =
                backStackEntry.arguments?.getString("idItems") ?: "0" // ✅ ดึงค่ามาจาก arguments
            DetailScreen(navController, idItems)
        }

    }


}




