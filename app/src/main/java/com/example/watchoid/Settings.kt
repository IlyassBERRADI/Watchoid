package com.example.watchoid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchoid.ui.theme.WatchoidTheme

class Settings : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            backgroundSettings()
            SettingsPage()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SettingsPage() {
        var isExpanded by remember { mutableStateOf(false) }
        var isExpanded2 by remember { mutableStateOf(false) }
        var isExpanded3 by remember { mutableStateOf(false) }
        var type by remember { mutableStateOf("") }
        val textFieldValue = remember { mutableStateOf(TextFieldValue()) }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Espacement horizontal entre les boutons et le menu déroulant
                horizontalArrangement = Arrangement.SpaceBetween // Espacement entre les boutons et le menu déroulant
            ) {
                // Menu déroulant
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {

                    TextField(
                        value = type,
                        onValueChange = {
                            type = it
                        },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(text = "HTTP")
                            },
                            onClick = {
                                type = "HTTP"
                                isExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = "TCP")
                            },
                            onClick = {
                                type = "TCP"
                                isExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = "UDP")
                            },
                            onClick = {
                                type = "UDP"
                                isExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = "ICMP")
                            },
                            onClick = {
                                type = "ICMP"
                                isExpanded = false
                            }
                        )
                    }
                }

                // Menu déroulant
                ExposedDropdownMenuBox(
                    expanded = isExpanded2,
                    onExpandedChange = { isExpanded2 = it },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {

                    TextField(
                        value = type,
                        onValueChange = {

                        },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded2)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded2,
                        onDismissRequest = { isExpanded2 = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(text = "Float")
                            },
                            onClick = {
                                type = "JSON"
                                isExpanded2 = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Espacement horizontal entre les boutons et le menu déroulant
                horizontalArrangement = Arrangement.SpaceBetween, // Espacement entre les boutons et le menu déroulant
                verticalAlignment = Alignment.CenterVertically
            ){
                // Zone de texte
                TextField(
                    value = textFieldValue.value,
                    onValueChange = {
                        textFieldValue.value = it
                    },
                    label = { Text("Périodicité") },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                // Menu déroulant
                ExposedDropdownMenuBox(
                    expanded = isExpanded3,
                    onExpandedChange = { isExpanded3 = it },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {

                    TextField(
                        value = type,
                        onValueChange = {
                            type = it
                        },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded3)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded3,
                        onDismissRequest = { isExpanded3 = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(text = "Float")
                            },
                            onClick = {
                                type = "JSON"
                                isExpanded3 = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Espacement horizontal entre les boutons et le menu déroulant
                horizontalArrangement = Arrangement.SpaceBetween // Espacement entre les boutons et le menu déroulant
            ){
                // Zone de texte
                TextField(
                    value = textFieldValue.value,
                    onValueChange = {
                        textFieldValue.value = it
                    },
                    label = { Text("Time before delete") },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                // Zone de texte
                TextField(
                    value = textFieldValue.value,
                    onValueChange = {
                        textFieldValue.value = it
                    },
                    label = { Text("Number of alert") },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
            }
        }
    }

    @Composable
    fun backgroundSettings(){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray))
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp) // Hauteur du rectangle
                    .align(Alignment.TopCenter) // Alignement en haut
                    .shadow( // Ajouter une ombre
                        elevation = 8.dp, // Taille de l'ombre
                        shape = RectangleShape, // Forme de l'ombre
                        clip = true // Découpe le contenu à la forme de l'ombre
                    )
                    .background(color = Color.LightGray) // Couleur du rectangle
            ) {
                Text(text = "Settings",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
