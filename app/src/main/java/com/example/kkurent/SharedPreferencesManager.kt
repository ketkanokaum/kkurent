package com.example.kkurent

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferencesManager(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = preferences.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = preferences.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()

    var idUsers: Int
        get() = preferences.getInt(KEY_USER_ID, 0)
        set(value) = preferences.edit().putInt(KEY_USER_ID, value).apply()

    var email: String?
        get() = preferences.getString(KEY_EMAIL, null)
        set(value) = preferences.edit().putString(KEY_EMAIL, value).apply()

    var password: String?
        get() = preferences.getString(KEY_PASSWORD, null)
        set(value) = preferences.edit().putString(KEY_PASSWORD, value).apply()

    var fnameLname: String?
        get() = preferences.getString(KEY_FNAME_LNAME, null)
        set(value) = preferences.edit().putString(KEY_FNAME_LNAME, value).apply()

    var address: String?
        get() = preferences.getString(KEY_ADDRESS, null)
        set(value) = preferences.edit().putString(KEY_ADDRESS, value).apply()

    var bank: String?
        get() = preferences.getString(KEY_BANK, null)
        set(value) = preferences.edit().putString(KEY_BANK, value).apply()

    var accountNumber: String?
        get() = preferences.getString(KEY_ACCOUNT_NUMBER, null)
        set(value) = preferences.edit().putString(KEY_ACCOUNT_NUMBER, value).apply()

    var accountName: String?
        get() = preferences.getString(KEY_ACCOUNT_NAME, null)
        set(value) = preferences.edit().putString(KEY_ACCOUNT_NAME, value).apply()

    var phone: String?
        get() = preferences.getString(KEY_PHONE, null)
        set(value) = preferences.edit().putString(KEY_PHONE, value).apply()

    var role: String?
        get() = preferences.getString(KEY_ROLE, null)
        set(value) = preferences.edit().putString(KEY_ROLE, value).apply()

    var del_status: String?
        get() = preferences.getString(KEY_DEL_STATUS, null)
        set(value) = preferences.edit().putString(KEY_DEL_STATUS, value).apply()

    val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    fun isCurrentUser(userId: Int): Boolean {
        return idUsers == userId
    }

    fun clearUserAll() {
        //preferences.edit {clear()}
        // ลบข้อมูลการเข้าสู่ระบบ เช่น isLoggedIn, userId, password
        preferences.edit().remove(KEY_IS_LOGGED_IN).apply()
        preferences.edit().remove(KEY_USER_ID).apply()
        preferences.edit().remove(KEY_PASSWORD).apply()
//        preferences.edit().remove(KEY_EMAIL).apply()
        preferences.edit().remove(KEY_FNAME_LNAME).apply()
        preferences.edit().remove(KEY_ROLE).apply()
    }

    fun clearUserLogin() {
        preferences.edit().remove(KEY_IS_LOGGED_IN).apply()
        preferences.edit().remove(KEY_USER_ID).apply()
        preferences.edit().remove(KEY_PASSWORD).apply()
        preferences.edit().remove(KEY_EMAIL).apply()
        preferences.edit().remove(KEY_FNAME_LNAME).apply()
        preferences.edit().remove(KEY_ROLE).apply()
    }

    fun saveUserData(
        idUsers: Int,
        context: Context,
        fullName: String,
        email: String,
        phone: String,
        address: String,
        bank: String,
        accountNumber: String,
        accountName: String,
        role: String,
        del_status: String
    ) {
        preferences.edit {
            putInt(KEY_USER_ID, idUsers)
            putString(KEY_FNAME_LNAME, fullName)
            putString(KEY_EMAIL, email)
            putString(KEY_PHONE, phone)
            putString(KEY_ADDRESS, address)
            putString(KEY_BANK, bank)
            putString(KEY_ACCOUNT_NUMBER, accountNumber)
            putString(KEY_ACCOUNT_NAME, accountName)
            putString(KEY_ROLE, role)
            putString(KEY_DEL_STATUS, del_status)
            putBoolean(KEY_IS_LOGGED_IN, true)
        }
    }

    companion object {
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val KEY_USER_ID = "user_id"
        const val KEY_EMAIL = "key_email"
        const val KEY_PASSWORD = "key_password"
        const val KEY_FNAME_LNAME = "key_fname_lname"
        const val KEY_ADDRESS = "key_address"
        const val KEY_BANK = "key_bank"
        const val KEY_ACCOUNT_NUMBER = "key_account_number"
        const val KEY_ACCOUNT_NAME = "key_account_name"
        const val KEY_PHONE = "key_phone"
        const val KEY_ROLE = "key_role"
        const val KEY_DEL_STATUS = "key_del_status"
    }
}