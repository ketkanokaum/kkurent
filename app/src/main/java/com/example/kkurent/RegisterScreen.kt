package com.example.kkurent

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {

    var isButtonEnabled by remember { mutableStateOf(false) }


    var idUsers by remember { mutableStateOf(0) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fname_lname by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val bank = remember { mutableStateOf("") }
    var account_number by remember { mutableStateOf("") }
    var account_name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var id_card_img by remember { mutableStateOf<Uri?>(null) }
    var profile_picture by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current


    val idCardLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        id_card_img = uri
        uri?.let {
            Log.d("DEBUG", "Selected ID Card Image: $it")
        } ?: run {
            Toast.makeText(context, "คุณไม่ได้เลือกรูปภาพบัตรประชาชน", Toast.LENGTH_SHORT).show()
        }
    }

    val profilePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profile_picture = uri
        uri?.let {
            Log.d("DEBUG", "Selected Profile Picture: $it")
        } ?: run {
            Toast.makeText(context, "คุณไม่ได้เลือกรูปภาพโปรไฟล์", Toast.LENGTH_SHORT).show()
        }
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val contextForToast = LocalContext.current
    val createClient = userApi.create()


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
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(65.dp),
                            modifier = Modifier.width(150.dp).height(65.dp)
                        ) {
                            Text("เข้าสู่ระบบ", color = Color.Black, fontSize = 17.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B366D)),
                            shape = RoundedCornerShape(65.dp),
                            modifier = Modifier.width(150.dp).height(65.dp)
                        ) {
                            Text("ลงทะเบียน", color = Color.White, fontSize = 17.sp)
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(90.dp)) }

            item {
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        isButtonEnabled = email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && fname_lname.isNotEmpty() &&
                                address.isNotEmpty() && bank.value.isNotEmpty() && account_number.isNotEmpty() && account_name.isNotEmpty() && phone.isNotEmpty() &&  id_card_img != null && profile_picture != null &&
                                password == confirmPassword // เช็คว่า password กับ confirmPassword ตรงกัน
                    },
                    label = {
                        Text(
                            "อีเมล",
                            color = Color.DarkGray.copy(alpha = 0.7f),
                            fontSize = 17.sp
                        )
                    },
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
                    modifier = Modifier.fillMaxWidth(0.8f).height(70.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.DarkGray.copy(alpha = 0.2f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }

            item { Spacer(modifier = Modifier.height(10.dp)) }

            item {
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        isButtonEnabled =  email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && fname_lname.isNotEmpty() &&
                                address.isNotEmpty() && bank.value.isNotEmpty() && account_number.isNotEmpty() && account_name.isNotEmpty() && phone.isNotEmpty() &&  id_card_img != null && profile_picture != null &&
                                password == confirmPassword // เช็คว่า password กับ confirmPassword ตรงกัน
                    },

                    label = {
                        Text(
                            "รหัสผ่าน",
                            color = Color.DarkGray.copy(alpha = 0.7f),
                            fontSize = 17.sp
                        )
                    },
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
                    modifier = Modifier.fillMaxWidth(0.8f).height(70.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.DarkGray.copy(alpha = 0.2f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        isButtonEnabled = confirmPassword.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && fname_lname.isNotEmpty() &&
                                address.isNotEmpty() && bank.value.isNotEmpty() && account_number.isNotEmpty() && account_name.isNotEmpty() && phone.isNotEmpty() &&  id_card_img != null && profile_picture != null &&
                                password == confirmPassword // เช็คว่า password กับ confirmPassword ตรงกัน
                    },
                    label = {
                        Text(
                            "ยืนยันรหัสผ่าน",
                            color = Color.DarkGray.copy(alpha = 0.7f),
                            fontSize = 17.sp
                        )
                    },
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
                    modifier = Modifier.fillMaxWidth(0.8f).height(70.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.DarkGray.copy(alpha = 0.2f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                OutlinedTextField(
                    value = fname_lname,
                    onValueChange = {
                        fname_lname = it
                        isButtonEnabled =  email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && fname_lname.isNotEmpty() &&
                                address.isNotEmpty() && bank.value.isNotEmpty() && account_number.isNotEmpty() && account_name.isNotEmpty() && phone.isNotEmpty() &&  id_card_img != null && profile_picture != null &&
                                password == confirmPassword // เช็คว่า password กับ confirmPassword ตรงกัน
                    },
                    label = {
                        Text(
                            "ชื่อ-นามสกุล",
                            color = Color.DarkGray.copy(alpha = 0.7f),
                            fontSize = 17.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth(0.8f).height(70.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.DarkGray.copy(alpha = 0.2f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                OutlinedTextField(
                    value = address,
                    onValueChange = {
                        address = it
                        isButtonEnabled = email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && fname_lname.isNotEmpty() &&
                                address.isNotEmpty() && bank.value.isNotEmpty() && account_number.isNotEmpty() && account_name.isNotEmpty() && phone.isNotEmpty() &&  id_card_img != null && profile_picture != null &&
                                password == confirmPassword // เช็คว่า password กับ confirmPassword ตรงกัน
                    },
                    label = {
                        Text(
                            "ที่อยู่",
                            color = Color.DarkGray.copy(alpha = 0.7f),
                            fontSize = 17.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth(0.8f).height(70.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.DarkGray.copy(alpha = 0.2f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp)) // เพิ่มระยะห่าง 16dp
            }

            item { BankDropdown(bank) }

            item {
                Spacer(modifier = Modifier.height(10.dp)) // เพิ่มระยะห่าง 16dp
            }

            item {
                OutlinedTextField(
                    value = account_number,
                    onValueChange = {
                        account_number = it
                        isButtonEnabled = email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && fname_lname.isNotEmpty() &&
                                address.isNotEmpty() && bank.value.isNotEmpty() && account_number.isNotEmpty() && account_name.isNotEmpty() && phone.isNotEmpty() &&  id_card_img != null && profile_picture != null &&
                                password == confirmPassword // เช็คว่า password กับ confirmPassword ตรงกัน
                    },
                    label = {
                        Text(
                            "เลขบัญชี",
                            color = Color.DarkGray.copy(alpha = 0.7f),
                            fontSize = 17.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth(0.8f).height(70.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.DarkGray.copy(alpha = 0.2f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp)) // เพิ่มระยะห่าง 16dp
            }
            item {
                OutlinedTextField(
                    value = account_name,
                    onValueChange = {
                        account_name = it
                        isButtonEnabled = email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && fname_lname.isNotEmpty() &&
                                address.isNotEmpty() && bank.value.isNotEmpty() && account_number.isNotEmpty() && account_name.isNotEmpty() && phone.isNotEmpty() &&  id_card_img != null && profile_picture != null &&
                                password == confirmPassword // เช็คว่า password กับ confirmPassword ตรงกัน
                    },
                    label = {
                        Text(
                            "ชื่อบัญชี",
                            color = Color.DarkGray.copy(alpha = 0.7f),
                            fontSize = 17.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth(0.8f).height(70.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.DarkGray.copy(alpha = 0.2f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp)) // เพิ่มระยะห่าง 16dp
            }

            item {
                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        isButtonEnabled = email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && fname_lname.isNotEmpty() &&
                                address.isNotEmpty() && bank.value.isNotEmpty() && account_number.isNotEmpty() && account_name.isNotEmpty() && phone.isNotEmpty() &&  id_card_img != null && profile_picture != null &&
                                password == confirmPassword // เช็คว่า password กับ confirmPassword ตรงกัน
                    },
                    label = {
                        Text(
                            "หมายเลขโทรศัพท์",
                            color = Color.DarkGray.copy(alpha = 0.7f),
                            fontSize = 17.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth(0.8f).height(70.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.DarkGray.copy(alpha = 0.2f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
                Box(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(top = 40.dp)
                        .width(200.dp)
                        .height(150.dp)
                ) {
                    Image(
                        painter = if (id_card_img != null) {
                            rememberAsyncImagePainter(id_card_img)
                        } else {
                            painterResource(id = R.drawable.image_search)
                        },
                        contentDescription = null,
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("เพิ่มรูปบัตรประชาชน", color = Color.Black, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Button(
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp),
                    onClick = {
                        idCardLauncher.launch("image/*")
                        isButtonEnabled = email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() &&
                                fname_lname.isNotEmpty() && address.isNotEmpty() && bank.value.isNotEmpty() &&
                                account_number.isNotEmpty() && account_name.isNotEmpty() && phone.isNotEmpty() &&
                                id_card_img != null && profile_picture != null &&
                                password == confirmPassword
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D346D))
                ) {
                    Text(text = "เปิดอัลบั้ม")
                }
                Spacer(modifier = Modifier.height(50.dp))
            }

            item {
                Box(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(top = 40.dp)
                        .width(200.dp)
                        .height(150.dp)
                ) {
                    Image(
                        painter = if (profile_picture != null) {
                            rememberAsyncImagePainter(profile_picture)
                        } else {
                            painterResource(id = R.drawable.image_search)
                        },
                        contentDescription = null,
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("เพิ่มรูปโปรไฟล์", color = Color.Black, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Button(
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp),
                    onClick = {
                        profilePictureLauncher.launch("image/*")
                        isButtonEnabled = email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() &&
                                fname_lname.isNotEmpty() && address.isNotEmpty() && bank.value.isNotEmpty() &&
                                account_number.isNotEmpty() && account_name.isNotEmpty() && phone.isNotEmpty() &&
                                id_card_img != null && profile_picture != null &&
                                password == confirmPassword
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D346D))
                ) {
                    Text(text = "เปิดอัลบั้ม")
                }
                Spacer(modifier = Modifier.height(50.dp))
            }


            item { Spacer(modifier = Modifier.height(50.dp)) }

            item {
                Button(
                    onClick = {
                        val inputStream = contextForToast.contentResolver.openInputStream(profile_picture!!)
                            ?: throw Exception("Failed to open input stream")
                        val imageFile = File.createTempFile("profile", ".jpg", contextForToast.cacheDir)
                        val outputStream = FileOutputStream(imageFile)

                        inputStream.copyTo(outputStream)
                        inputStream.close()
                        outputStream.close()
                        val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())

                        // ✅ จัดการ ID Card Image (แก้ให้ใช้ id_card_img)
                        val inputStream2 = contextForToast.contentResolver.openInputStream(id_card_img!!)
                            ?: throw Exception("Failed to open input stream")
                        val idCardFile = File.createTempFile("id_card", ".jpg", contextForToast.cacheDir)
                        val outputStream2 = FileOutputStream(idCardFile)

                        inputStream2.copyTo(outputStream2) // ✅ แก้ให้ใช้ outputStream2
                        inputStream2.close()
                        outputStream2.close()
                        val requestBody2 = idCardFile.asRequestBody("image/jpeg".toMediaTypeOrNull()) // ✅ ใช้ idCardFile

                        val profilePicturePart = MultipartBody.Part.createFormData("profile_picture", imageFile.name, requestBody)
                        val idCardImgPart = MultipartBody.Part.createFormData("id_card_img", idCardFile.name, requestBody2)

                        val role = "user".toRequestBody("text/plain".toMediaTypeOrNull()) // กำหนด role เป็น "user"
                        val fnameLnamePart = fname_lname.toRequestBody("text/plain".toMediaTypeOrNull())
                        val emailPart = email.toRequestBody("text/plain".toMediaTypeOrNull())
                        val addressPart = address.toRequestBody("text/plain".toMediaTypeOrNull())
                        val bankPart = bank.value.toRequestBody("text/plain".toMediaTypeOrNull())
                        val accountNumberPart = account_number.toRequestBody("text/plain".toMediaTypeOrNull())
                        val accountNamePart = account_name.toRequestBody("text/plain".toMediaTypeOrNull())
                        val passwordPart = password.toRequestBody("text/plain".toMediaTypeOrNull())
                        val phonePart = phone.toRequestBody("text/plain".toMediaTypeOrNull())
                        // ✅ สร้าง RequestBody สำหรับ idUsers
                        val idUsersPart = idUsers.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                        createClient.insertUser(
                            idUsersPart,
                            fnameLnamePart,
                            emailPart,
                            addressPart,
                            bankPart,
                            accountNumberPart,
                            accountNamePart,      // ✅ ชื่อบัญชี
                            passwordPart,         // ✅ รหัสผ่าน
                            phonePart,            // ✅ เบอร์โทรศัพท์
                            profilePicturePart,   // ✅ รูปโปรไฟล์ (เคลื่อนจากตำแหน่งบนลงมา)
                            idCardImgPart,        // ✅ รูปบัตรประชาชน
                            role                  // ✅ บทบาท


                        ).enqueue(object : Callback<Register> {
                            override fun onResponse(
                                call: Call<Register>,
                                response: Response<Register>
                            ) {

                                //ตรงที่เพิ่มค่า
                                if (response.isSuccessful) {
                                    val loginResponse = response.body()
                                    if (loginResponse != null) {
                                        val sharedPreferencesManager = SharedPreferencesManager(context)
                                        sharedPreferencesManager.saveUserData(
                                            idUsers = loginResponse.idUsers ?: 0,  // เพิ่ม idUsers
                                            context = context,
                                            fullName = loginResponse.fname_lname ?: "",
                                            email = loginResponse.email ?: "",
                                            phone = loginResponse.phone ?: "",
                                            address = loginResponse.address ?: "",
                                            bank = loginResponse.bank ?: "",
                                            accountNumber = loginResponse.account_number ?: "",
                                            accountName = loginResponse.account_name ?: "",
                                            role = loginResponse.role ?: "",
                                            del_status = loginResponse.del_status ?:""
                                        )
                                    }

                                // แจ้งเตือนการลงทะเบียนสำเร็จ
                                    Toast.makeText(
                                        contextForToast,
                                        "ลงทะเบียนสำเร็จ! โปรดเข้าสู่ระบบเพื่อเข้าใช้งานแอปพลิเคชัน",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // นำทางไปยังหน้าล็อกอิน
                                    navController.navigate(Screen.Login.route)
                                }
                            }
                            override fun onFailure(call: Call<Register>, t: Throwable) {
                                Log.e("NETWORK_ERROR", "Failed: ${t.message}", t)
                                Toast.makeText(contextForToast, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                            }
                        })
                    },
                    enabled = isButtonEnabled,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B366D)),
                    modifier = Modifier.fillMaxWidth(0.8f).height(60.dp)
                ) {
                    Text("ลงทะเบียน", color = Color.White, fontSize = 17.sp)
                }
            }
            item { Spacer(modifier = Modifier.height(40.dp)) }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo3),
                        contentDescription = "Icon Rent",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(90.dp).padding(bottom = 40.dp)
                    )
                }
            }
        }
    }
}

fun createMultipartFromUri(context: Context, uri: Uri?, name: String): MultipartBody.Part? {
    uri ?: return null

    val file = File.createTempFile(name, ".jpg", context.cacheDir)
    val inputStream = context.contentResolver.openInputStream(uri) ?: return null
    val outputStream = FileOutputStream(file)

    inputStream.use { it.copyTo(outputStream) }

    val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(name, file.name, requestBody)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankDropdown(selectedBank: MutableState<String>) {
    val bankList = listOf(
        "ธนาคารกรุงไทย (KTB)",
        "ธนาคารกรุงเทพ (BBL)",
        "ธนาคารกรุงศรีอยุธยา (BAY)",
        "ธนาคารกสิกรไทย (KBANK)",
        "ธนาคารทิสโก้ (TISCO)",
        "ธนาคารเกียรตินาคินภัทร (KKP)",
        "ธนาคารซีไอเอ็มบี ไทย (CIMBT)",
        "ธนาคารทหารไทยธนชาต (TTB)",
        "ธนาคารไทยพาณิชย์ (SCB)",
        "ธนาคารยูโอบี (UOB)",
        "ธนาคารแลนด์ แอนด์ เฮ้าส์ (LH)",
        "ธนาคารสแตนดาร์ดชาร์เตอร์ด (ไทย) (SC)"
    )

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .width(336.dp)
                .menuAnchor()
                .fillMaxWidth(0.8f)
                .height(70.dp),
            shape = RoundedCornerShape(25.dp),
            readOnly = true, // ✅ ย้ายไปอยู่ในพารามิเตอร์ที่ถูกต้อง
            value = selectedBank.value,
            onValueChange = {},
            label = { Text("เลือกธนาคาร") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.DarkGray.copy(alpha = 0.2f),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            bankList.forEach { bank ->
                DropdownMenuItem(
                    text = { Text(bank) },
                    onClick = {
                        selectedBank.value = bank
                        expanded = false
                    }
                )
            }
        }
    }
}


