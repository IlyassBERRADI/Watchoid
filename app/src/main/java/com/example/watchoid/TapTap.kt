package com.example.watchoid

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchoid.composant.Background
import com.example.watchoid.entity.TapTapGames
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TapTap : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Background(text = "TapTap", main = false)
            Game()
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Game() {
    var clickCount by remember { mutableStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(10) }
    var idGame by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    var countdown by remember { mutableStateOf(3) }
    var isCountingDown by remember { mutableStateOf(true) }

    LaunchedEffect(Unit, idGame) {
        while (countdown > 0) {
            delay(1000L)
            countdown--
        }
        isCountingDown = false
        scope.launch {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            isGameOver = true
        }
    }

    if (isCountingDown) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Game starts in $countdown", fontSize = 40.sp, modifier = Modifier.padding(20.dp))
        }
    }
    else if (isGameOver) {
        GameOverScreen(clickCount) {
            clickCount = 0
            isGameOver = false
            timeLeft = 10
            idGame++
            countdown = 3
            isCountingDown = true
        }
    } else {
        GameScreen(clickCount, timeLeft) {
            clickCount++
        }
    }
}

@Composable
fun GameScreen(clickCount: Int, time: Int, onBoxClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onBoxClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Time Remaining = $time", fontSize = 30.sp, modifier = Modifier.padding(20.dp))
            Text(text = "Count = $clickCount", fontSize = 30.sp, modifier = Modifier.padding(20.dp))
        }
    }
}

@Composable
fun GameOverScreen(clickCount: Int, onPlayAgain: () -> Unit) {
    val getTest = rememberCoroutineScope()
    var test = remember { mutableStateListOf<TapTapGames>() }

    LaunchedEffect(Unit) {
        getTest.launch(IO) {
            val record = TapTapGames(record = clickCount)
            MainActivity.database.taptapTable().insert(record)
            val list = MainActivity.database.taptapTable().getAllTests()
            test.clear()
            test.addAll(list)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Game Over!", fontSize = 40.sp, modifier = Modifier.padding(20.dp))
            Text(text = "Your score: $clickCount", fontSize = 30.sp, modifier = Modifier.padding(20.dp))

            Text(text = "Scoreboard", fontSize = 30.sp, modifier = Modifier.padding(20.dp))
            test.forEach { score ->
                Text(text = "N°${score.id_test}. Score: ${score.record}")
            }

            Button(onClick = onPlayAgain, shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E698A))) {
                Text(text = "Play Again")
            }
        }
    }
}
