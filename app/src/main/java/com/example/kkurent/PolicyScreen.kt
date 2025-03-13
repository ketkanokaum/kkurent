package com.example.kkurent

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.kkurent.data.Chat
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolicyScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp) // เพิ่ม padding เพื่อให้เนื้อหาไม่ชิดขอบ
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            // ปุ่มย้อนกลับ
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            // หัวข้อหลัก
            Text(
                text = "📜 นโยบายการใช้งานของแอป Around KKU Rent",
                style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    PolicySection(
                        title = "1. บทบาทของแอปพลิเคชัน",
                        content = "Around KKU Rent เป็นแพลตฟอร์มตัวกลางที่ช่วยเชื่อมต่อผู้ปล่อยเช่าและผู้เช่าให้สามารถติดต่อกันได้โดยสะดวก อย่างไรก็ตาม แอปไม่ได้มีส่วนเกี่ยวข้องโดยตรงกับธุรกรรมทางการเงิน การตกลงเช่า หรือการส่งมอบสินค้า ทั้งหมดเป็นความรับผิดชอบของผู้ใช้แต่ละฝ่าย"
                    )
                }
                item {
                    PolicySection(
                        title = "2. การทำธุรกรรมและความรับผิดชอบ",
                        content = "ผปล่อยเช่าและผู้เช่ามีหน้าที่ตรวจสอบข้อมูลและพิจารณาความน่าเชื่อถือของคู่สัญญาด้วยตนเองก่อนดำเนินการเช่าหรือให้เช่า กรุณาตรวจสอบรายละเอียดสินค้า เงื่อนไขการเช่า และวิธีการชำระเงินอย่างรอบคอบ ทางแอป ไม่มีส่วนเกี่ยวข้องกับการชำระเงินหรือการรับประกันใดๆ และจะไม่รับผิดชอบในกรณีที่เกิดความเสียหาย การฉ้อโกง หรือข้อพิพาทใดๆ ระหว่างผู้ใช้"
                    )
                }
                item {
                    PolicySection(
                        title = "3. การป้องกันและข้อควรระวัง",
                        content = """
                            • หลีกเลี่ยงการโอนเงินล่วงหน้าโดยไม่มีหลักฐานที่น่าเชื่อถือ
                            • นัดรับสินค้าด้วยตนเองในสถานที่ปลอดภัย
                            • ตรวจสอบรายละเอียดของสินค้าและเอกสารให้ครบถ้วนก่อนชำระเงิน
                        """.trimIndent()
                    )
                }
            }
        }
    }
}

@Composable
fun PolicySection(title: String, content: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray),
        )
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp), color = Color.LightGray)
    }
}
