package com.example.kkurent

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    lateinit var sharedPreferences: SharedPreferencesManager

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isButtonEnabled by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val createClient = userApi.create()

    val contextForToast = LocalContext.current.applicationContext
    var usersItems by remember { mutableStateOf<Login?>(null) } // เปลี่ยนเป็น nullable และใช้ by remember

    sharedPreferences = SharedPreferencesManager(contextForToast)

    // Lifecycle State
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {
            }
            Lifecycle.State.INITIALIZED -> {
            }
            Lifecycle.State.CREATED -> {
            }
            Lifecycle.State.STARTED -> {
            }
            Lifecycle.State.RESUMED -> { // ใช้ RESUMED state เท่านั้น
//                if (sharedPreferences.isLoggedIn) {
//                    navController.navigate(Screen.Profile.route)
//                } else if (!sharedPreferences.userId.isNullOrEmpty()) {
//                    email = sharedPreferences.userId!!
//                }
                // โหลด email มาใส่
                email = sharedPreferences.email ?: ""
            }
            else -> {}
        }
    }



    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 55.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { navController.navigate(Screen.Login.route) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B366D)),
                            shape = RoundedCornerShape(65.dp),
                            modifier = Modifier.width(150.dp).height(65.dp)
                        ) {
                            Text("เข้าสู่ระบบ", color = Color.White, fontSize = 17.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = { navController.navigate(Screen.Register.route) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(65.dp),
                            modifier = Modifier.width(150.dp).height(65.dp)
                        ) {
                            Text("ลงทะเบียน", color = Color.Black, fontSize = 17.sp)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(60.dp)) }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            isButtonEnabled = email.isNotEmpty() && password.isNotEmpty()
                        },
                        label = { Text("อีเมล", fontSize = 17.sp) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        shape = RoundedCornerShape(25.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.DarkGray.copy(alpha = 0.2f),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            isButtonEnabled = email.isNotEmpty() && password.isNotEmpty()
                        },
                        label = { Text("รหัสผ่าน", fontSize = 17.sp) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        shape = RoundedCornerShape(25.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.DarkGray.copy(alpha = 0.2f),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )

                    Button(
                        onClick = {
                            Log.d("Login", "Login button clicked")
                            keyboardController?.hide()
                            focusManager.clearFocus()

                            Log.d("Login", "Calling loginUser API with email: $email")
                            createClient.loginUser(email, password).enqueue(object : Callback<Login> {
                                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                                    Log.d("API Response", "Response received: ${response.code()}")

                                    response.body()?.let { loginResponse ->
                                        Log.d("API Response", "Login response: success=${loginResponse.success}, idUsers=${loginResponse.idUsers} , Name=${loginResponse.fname_lname}, role=${loginResponse.role}")

                                        if (loginResponse.success == 1) {
                                            sharedPreferences.idUsers = loginResponse.idUsers
                                            sharedPreferences.isLoggedIn = true
                                            sharedPreferences.fnameLname = loginResponse.fname_lname
                                            sharedPreferences.role = loginResponse.role
                                            sharedPreferences.email = loginResponse.email

                                            Log.d("Login", "Login successful, navigating to Profile")
                                            Toast.makeText(contextForToast, "เข้าสู่ระบบสำเร็จ!", Toast.LENGTH_LONG).show()
                                            navController.currentBackStackEntry?.savedStateHandle?.set<Int>(
                                                "selectedScreen",
                                                4 // active profile button
                                            )
                                            navController.navigate(Screen.Profile.route)
                                        } else if (loginResponse.success == 0) {
                                            Log.w("Login", "Incorrect email or password")
                                            Toast.makeText(contextForToast, "อีเมลหรือรหัสผ่านของคุณไม่ถูกต้องหรือบัญชีถูกลบ", Toast.LENGTH_LONG).show()
                                        } else {
                                            Log.w("Login", "Email not found")
                                            Toast.makeText(contextForToast, "ไม่พบอีเมลของคุณ", Toast.LENGTH_LONG).show()
                                        }

                                    } ?: run {
                                        Log.e("Login", "Login failed - Response body is null")
                                        Toast.makeText(contextForToast, "ไม่สามารถเข้าสู่ระบบได้ โปรดลองอีกครั้ง", Toast.LENGTH_LONG).show()
                                    }
                                }

                                override fun onFailure(call: Call<Login>, t: Throwable) {
                                    Log.e("Login", "Login request failed: ${t.message}")
                                    Toast.makeText(contextForToast, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                                }
                            })
                        },
                        enabled = isButtonEnabled,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B366D)),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(60.dp),
                        shape = RoundedCornerShape(25.dp),
                    ) {
                        Text("เข้าสู่ระบบ", color = Color.White, fontSize = 17.sp)
                    }

                }

            }
            // ย้าย Box ที่มี Image เข้าไปใน LazyColumn
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 40.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo3),
                        contentDescription = "Icon Rent",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(90.dp)
                    )
                }
            }
        }
    }
}