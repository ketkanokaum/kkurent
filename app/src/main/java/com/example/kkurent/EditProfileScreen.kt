package com.example.kkurent

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.lang.reflect.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  EditProfileScreen(navController: NavController) {
//
//
//    var name by remember { mutableStateOf(user.name) }
//    var address by remember { mutableStateOf(user.address) }
//    var bankName by remember { mutableStateOf(user.bankName) }
//    var accountNumber by remember { mutableStateOf(user.accountNumber) }
//    var accountHolder by remember { mutableStateOf(user.accountHolderName) }
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        TopAppBar(
//            title = { Text("แก้ไขโปรไฟล์") },
//            navigationIcon = {
//                IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                }
//            }
//        )
//
//        TextField(value = name, onValueChange = { name = it }, label = { Text("ชื่อ-นามสกุล") })
//        TextField(value = address, onValueChange = { address = it }, label = { Text("ที่อยู่") })
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(text = "ข้อมูลบัญชีธนาคาร", fontWeight = FontWeight.Bold)
//        TextField(value = bankName, onValueChange = { bankName = it }, label = { Text("ธนาคาร") })
//        TextField(value = accountNumber, onValueChange = { accountNumber = it }, label = { Text("เลขบัญชี") })
//        TextField(value = accountHolder, onValueChange = { accountHolder = it }, label = { Text("ชื่อบัญชี") })
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = {
//            profileViewModel.updateProfile(name, address)
//            profileViewModel.updateBankInfo(bankName, accountNumber, accountHolder)
//            navController.popBackStack()
//        }) {
//            Text("บันทึก")
//        }
//    }
}
