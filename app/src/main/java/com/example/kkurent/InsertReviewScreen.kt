package com.example.kkurent

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kkurent.Review

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertReviewScreen(navController: NavController, reviews: MutableList<Review>) {
    var reviewText by remember { mutableStateOf("") } // เก็บข้อความรีวิว

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("เพิ่มรีวิวใหม่", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D346D), titleContentColor = Color.White)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("เขียนรีวิวของคุณ", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    label = { Text("รีิวิว") },
                    placeholder = { Text("พิมพ์รีวิวของคุณที่นี่...") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4,
                    singleLine = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (reviewText.isNotBlank()) {
                            reviews.add(Review(reviews.size + 1, reviewText, 105, 205, 305))
                            navController.popBackStack() // กลับไปที่หน้าก่อนหน้า
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D346D))
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Submit", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("บันทึกรีวิว", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    )
}

