//package com.example.kkurent
//
//import androidx.lifecycle.ViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//
//data class UserProfile(
//    val name: String = "Ponoponoyaa",
//    val email: String = "nuchsara.s@kkumail.com",
//    val address: String = "หอ หัสนัยต์99/33 กังสดาล ใกล้รัตติกาล หอหญิง รวมหอ",
//    val bankName: String = "กรุงไทย",
//    val accountNumber: String = "123456789",
//    val accountHolderName: String = "สรนุช ธิษณา"
//)
//
//class ProfileViewModel : ViewModel() {
//    private val _user = MutableStateFlow(UserProfile())
//    val user = _user.asStateFlow()
//
//    fun updateProfile(name: String, address: String) {
//        _user.value = _user.value.copy(name = name, address = address)
//    }
//
//    fun updateBankInfo(bankName: String, accountNumber: String, accountHolderName: String) {
//        _user.value = _user.value.copy(bankName = bankName, accountNumber = accountNumber, accountHolderName = accountHolderName)
//    }
//}
