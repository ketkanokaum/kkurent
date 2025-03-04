package com.example.kkurent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kkurent.Review

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewListScreen(navController: NavController, reviews: SnapshotStateList<Review>) {
    val reviews = remember {
        mutableStateListOf(
            Review(1, "Great service!", 101, 201, 301),
            Review(2, "Could be better", 102, 202, 302),
            Review(3, "Highly recommended!", 103, 203, 303),
            Review(4, "Not worth the price", 104, 204, 304)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("รีวิวทั้งหมด", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D346D), titleContentColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                   
                    .padding(end = 16.dp, bottom = 16.dp),
                onClick = {navController.navigate(Screen.InsertReview.route) }, // ไปหน้าเพิ่ม
                containerColor = Color(0xFFF59949),
                shape = CircleShape // ทำให้เป็นทรงกลม
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add icon",
                    tint = Color.White
                )
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF5F5F5)),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(reviews) { review ->
                    ReviewCard(review) {
                        // onClick action for later database submission
                        println("Clicked on review: ${review.review_comment}")
                    }
                }
            }
        }
    )
}

@Composable
fun ReviewCard(review: Review, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = review.review_comment, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Rental ID: ${review.Rental_idRental}", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

