package com.example.kkurent

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatDate(dateString: String?): String {
    return try {
        DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(Instant.parse(dateString))
    } catch (e: Exception) {
        ""
    }
}

fun formatTime(dateString: String?): String {
    return try {
        DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault()).format(Instant.parse(dateString))
    } catch (e: Exception) {
        ""
    }
}

fun formatDateTime(dateString: String?): String {
    return try {
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault()).format(Instant.parse(dateString))
    } catch (e: Exception) {
        ""
    }
}