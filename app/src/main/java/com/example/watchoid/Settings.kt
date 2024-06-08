package com.example.watchoid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.watchoid.composant.Background
import com.example.watchoid.composant.DropDownMenu
import com.example.watchoid.composant.InputTextField
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

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
            val context = LocalContext.current
            val items = listOf("HTTP", "UDP", "TCP", "ICMP")
            var protocol = remember { mutableStateOf("HTTP") }
            val coroutineScope = rememberCoroutineScope()
            val items2 = listOf("Secondes", "Min", "Heure", "Jour")
            var type2 = remember { mutableStateOf("Min") }
            var type3 = remember { mutableStateOf("Min") }
            var inputValue = remember { mutableStateOf("0") }
            var timeDelete = remember { mutableStateOf("0") }
            var nbError = remember { mutableStateOf("0") }
            DropDownMenu(items, protocol, onValueChange = {
                coroutineScope.launch(IO) {
                    val settings = MainActivity.database.settingsTable().getSettingByProtocol(it)
                    if (settings != null) {
                        inputValue.value = settings.periodicity.toString()
                        type2.value = settings.timeUnitPeriodicity
                        timeDelete.value = settings.timeBeforeDeletion.toString()
                        type3.value = settings.timeUnitDeletion
                        nbError.value = settings.nbError.toString()
                    }

                }

            })

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Périodicité :")
            Row (
                modifier = Modifier
                    .fillMaxWidth()
            ){

                //TextField(value = initialSetting?.periodicity.toString(), onValueChange = )
                InputTextField(inputValue, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                DropDownMenu(items2, type2, Modifier.weight(1f));
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Time before delete :")

            Row (
                modifier = Modifier
                    .fillMaxWidth()
            ){
                InputTextField(timeDelete, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                DropDownMenu(items2, type3, Modifier.weight(1f));
            }


            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Number of errors before alert :")

            InputTextField( nbError)
            Button(onClick = {
                coroutineScope.launch {
                    /*when(type2.value){
                        "Min" -> inputValue.value=(inputValue.value.toInt()*60).toString()
                        "Heure" -> inputValue.value=(inputValue.value.toInt()*60*60).toString()
                        "Jour" ->  inputValue.value=(inputValue.value.toInt()*24*60*60).toString()
                    }
                    when(type3.value){
                        "Min" -> timeDelete.value=(timeDelete.value.toInt()*60).toString()
                        "Heure" -> timeDelete.value=(timeDelete.value.toInt()*60*60).toString()
                        "Jour" ->  timeDelete.value=(timeDelete.value.toInt()*24*60*60).toString()
                    }*/
                    var idstg = MainActivity.database.settingsTable().getSettingByProtocol(protocol.value)?.idSetting
                    var setting = idstg?.let {
                        com.example.watchoid.entity.Settings(idSetting = it, protocol = protocol.value, periodicity = inputValue.value.toInt(),
                            timeUnitPeriodicity = type2.value, timeBeforeDeletion = timeDelete.value.toInt(),
                            timeUnitDeletion = type3.value, nbError = nbError.value.toInt())
                    }
                    if (setting != null) {
                        MainActivity.database.settingsTable().update(setting)
                    }
                }
                Toast.makeText(context, "Configuration enregistré", Toast.LENGTH_SHORT).show()
            }) {
                Text(text = "Enregistrer")
            }
        }
    }
}

