package com.example.kkurent


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kkurent.R
import com.example.kkurent.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ส่วนหัว
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .shadow(8.dp, RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                    .clip(RoundedCornerShape(bottomStart = 45.dp, bottomEnd = 45.dp))
                    .background(Color(0xFFFFA14A)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 55.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 30.dp, top = 30.dp),
                        onClick = { navController.navigate(Screen.Login.route) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "ลืมรหัสผ่าน",
                        color = Color.White,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }

        // คำอธิบาย
        item {
            Text(
                text = "กรุณาใส่อีเมลของคุณ เราจะทำการส่งลิงค์\nเพื่อตั้งรหัสใหม่ไปทางอีเมลของคุณ",
                color = Color.DarkGray.copy(alpha = 0.8f),
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        // ช่องกรอกอีเมล
        item {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("อีเมล", color = Color.DarkGray.copy(alpha = 0.7f)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(70.dp),
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.DarkGray.copy(alpha = 0.2f),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }

        item { Spacer(modifier = Modifier.height(50.dp)) }

        // ปุ่มยืนยัน
        item {
            Button(
                onClick = { navController.navigate(Screen.Login.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B366D)),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(60.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("ยืนยัน", color = Color.White, fontSize = 17.sp)
            }
        }

        item { Spacer(modifier = Modifier.height(50.dp)) }

        // โลโก้
        item {
            Image(
                painter = painterResource(id = R.drawable.logo3),
                contentDescription = "Icon Rent",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(90.dp)
                    .padding(bottom = 40.dp)
            )
        }
    }
}
