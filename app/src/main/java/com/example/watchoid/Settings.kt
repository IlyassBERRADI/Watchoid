package com.example.watchoid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchoid.composant.Background
import com.example.watchoid.composant.DropDownMenu
import com.example.watchoid.composant.InputTextField

class Settings : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Background(text = "Settings", main = false)
            SettingsPage()
        }
    }

    @Composable
    fun SettingsPage() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Protocole :")
            val items = listOf("HTTP", "UDP", "TCP", "ICMP")
            var type = remember { mutableStateOf("HTTP") }
            DropDownMenu(items, type);
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Périodicité :")
            Row (
                modifier = Modifier
                    .fillMaxWidth()
            ){
                val items = listOf("Min", "Heure", "Année")
                var type = remember { mutableStateOf("Min") }
                var inputValue = remember { mutableStateOf("0") }

                InputTextField(inputValue, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                DropDownMenu(items, type, Modifier.weight(1f));
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Time before delete :")
            var timeDelete = remember { mutableStateOf("0") }
            InputTextField(timeDelete)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Number of alert :")
            var nbAlert = remember { mutableStateOf("0") }
            InputTextField( nbAlert)
        }
    }
}
