package com.example.watchoid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.watchoid.ui.theme.WatchoidTheme

class TicTacToe : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchoidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TicTacToeGame()
                }
            }
        }
    }
}


@Composable
fun TicTacToeGame(){
    var player1 by remember {
        mutableStateOf(true)
    }
    var player2 by remember {
        mutableStateOf(false)
    }
    var winner1 by remember {
        mutableStateOf(false)
    }
    var winner2 by remember {
        mutableStateOf(false)
    }
    var playerCell by remember {
        mutableStateOf(Array(3) { Array<Int>(3) { 0 } })
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "TIC TAC TOE", fontSize = 50.sp, fontWeight = FontWeight.Bold)
        if (player1){
            Text(text = "Player's 1 turn", fontSize = 24.sp)
        }
        else if (player2)
            Text(text = "Player's 2 turn", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(30.dp))
        GameTable(player1, player2, {
            player1=!player1
            player2=!player2
        }, {winner1=true}, {winner2=true}, playerCell)
        Spacer(modifier = Modifier.height(40.dp))
        if (winner1) {
            for (i in 0 until 3){
                for (j in 0 until 3){
                    if (playerCell[i][j]==0){
                        playerCell[i][j] = -1
                    }
                }
            }
            Text(text = "Player 1 won!!!", fontSize = 34.sp)
            Button(onClick = {
                playerCell = Array(3) { Array<Int>(3) { 0 } }
                winner1 = false
                player1 = true
                player2 = false
            }) {
                Text(text = "Restart")
            }
        }
        else if (winner2) {
            Text(text = "Player 2 won!!!", fontSize = 34.sp)
            for (i in 0 until 3){
                for (j in 0 until 3){
                    if (playerCell[i][j]==0){
                        playerCell[i][j] = -1
                    }
                }
            }
            Button(onClick = {
                playerCell = Array(3) { Array<Int>(3) { 0 } }
                winner2 = false
                player1 = true
                player2 = false
            }) {
                Text(text = "Restart")
            }
        }
    }

}


@Composable
fun GameTable(player1 : Boolean, player2 : Boolean, onPlay : ()->Unit, onWin1 : ()-> Unit, onWin2 : ()->Unit, playerCell : Array<Array<Int>>){

    Box(modifier = Modifier.size(300.dp)){
        Canvas(modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
            ) {

            drawLine(
                color = Color.Black,
                start = Offset(0f, size.height / 3),
                end = Offset(size.width, size.height / 3),
                strokeWidth = 4f
            )
            drawLine(
                color = Color.Black,
                start = Offset(0f, 2 * size.height / 3),
                end = Offset(size.width, 2 * size.height / 3),
                strokeWidth = 4f
            )
            drawLine(
                color = Color.Black,
                start = Offset(size.width / 3, 0f),
                end = Offset(size.width / 3, size.height),
                strokeWidth = 4f
            )
            drawLine(
                color = Color.Black,
                start = Offset(2 * size.width / 3, 0f),
                end = Offset(2 * size.width / 3, size.height),
                strokeWidth = 4f
            )
            for (row in 0 until 3) {
                if (playerCell[row][0]==1 && playerCell[row][1]==1 && playerCell[row][2]==1) {
                    onWin1()
                    drawLine(
                        color = Color.Green,
                        start = Offset(0f, size.height/6+row*size.height/3),
                        end = Offset(size.width, size.height/6+row*size.height/3),
                        strokeWidth = 4f
                    )
                }
                if (playerCell[row][0]==2 && playerCell[row][1]==2 && playerCell[row][2]==2) {
                    onWin2()
                    drawLine(
                        color = Color.Green,
                        start = Offset(0f, size.height/6+row*size.height/3),
                        end = Offset(size.width, size.height/6+row*size.height/3),
                        strokeWidth = 4f
                    )
                }
            }

            for (col in 0 until 3) {
                if (playerCell[0][col]==1 && playerCell[1][col]==1 && playerCell[2][col]==1) {
                    onWin1()
                    drawLine(
                        color = Color.Green,
                        start = Offset(size.width/6+col*size.width/3, 0f),
                        end = Offset(size.width/6+col*size.width/3, size.height),
                        strokeWidth = 4f
                    )
                }
                if (playerCell[0][col]==2 && playerCell[1][col]==2 && playerCell[2][col]==2) {
                    onWin2()
                    drawLine(
                        color = Color.Green,
                        start = Offset(size.width/6+col*size.width/3, 0f),
                        end = Offset(size.width/6+col*size.width/3, size.height),
                        strokeWidth = 4f
                    )
                }
            }

            if (playerCell[0][0]==1 && playerCell[1][1]==1 && playerCell[2][2]==1) {
                onWin1()
                drawLine(
                    color = Color.Green,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = 4f
                )
            }
            if (playerCell[0][2]==1 && playerCell[1][1]==1 && playerCell[2][0]==1) {
                onWin1()
                drawLine(
                    color = Color.Green,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, 0f),
                    strokeWidth = 4f
                )
            }
            if (playerCell[0][0]==2 && playerCell[1][1]==2 && playerCell[2][2]==2) {
                onWin2()
                drawLine(
                    color = Color.Green,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = 4f
                )
            }
            if (playerCell[0][2]==2 && playerCell[1][1]==2 && playerCell[2][0]==2) {
                onWin2()
                drawLine(
                    color = Color.Green,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, 0f),
                    strokeWidth = 4f
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Box(modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        if (playerCell[0][0] == 0) {
                            if (player1)
                                playerCell[0][0] = 1
                            else if (player2)
                                playerCell[0][0] = 2
                            onPlay()
                        }

                    }) {
                    if (playerCell[0][0] == 1){
                        X()
                    }
                    else if (playerCell[0][0] == 2){
                        O()
                    }
                }
                Box(modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        if (playerCell[0][1] == 0) {
                            if (player1)
                                playerCell[0][1] = 1
                            else if (player2)
                                playerCell[0][1] = 2
                            onPlay()
                        }

                    }) {
                    if (playerCell[0][1] == 1){
                        X()
                    }
                    else if (playerCell[0][1] == 2){
                        O()
                    }
                }
                Box(modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        if (playerCell[0][2] == 0) {
                            if (player1)
                                playerCell[0][2] = 1
                            else if (player2)
                                playerCell[0][2] = 2
                            onPlay()
                        }

                    }) {
                    if (playerCell[0][2] == 1){
                        X()
                    }
                    else if (playerCell[0][2] == 2){
                        O()
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Box(modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        if (playerCell[1][0] == 0) {
                            if (player1)
                                playerCell[1][0] = 1
                            else if (player2)
                                playerCell[1][0] = 2
                            onPlay()
                        }

                    }) {
                    if (playerCell[1][0] == 1){
                        X()
                    }
                    else if (playerCell[1][0] == 2){
                        O()
                    }
                }
                Box(modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        if (playerCell[1][1] == 0) {
                            if (player1)
                                playerCell[1][1] = 1
                            else if (player2)
                                playerCell[1][1] = 2
                            onPlay()
                        }

                    }) {
                    if (playerCell[1][1] == 1){
                        X()
                    }
                    else if (playerCell[1][1] == 2){
                        O()
                    }
                }
                Box(modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        if (playerCell[1][2] == 0) {
                            if (player1)
                                playerCell[1][2] = 1
                            else if (player2)
                                playerCell[1][2] = 2
                            onPlay()
                        }

                    }) {
                    if (playerCell[1][2] == 1){
                        X()
                    }
                    else if (playerCell[1][2] == 2){
                        O()
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Box(modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        if (playerCell[2][0] == 0) {
                            if (player1)
                                playerCell[2][0] = 1
                            else if (player2)
                                playerCell[2][0] = 2
                            onPlay()
                        }

                    }) {
                    if (playerCell[2][0] == 1){
                        X()
                    }
                    else if (playerCell[2][0] == 2){
                        O()
                    }
                }
                Box(modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        if (playerCell[2][1] == 0) {
                            if (player1)
                                playerCell[2][1] = 1
                            else if (player2)
                                playerCell[2][1] = 2
                            onPlay()
                        }

                    }) {
                    if (playerCell[2][1] == 1){
                        X()
                    }
                    else if (playerCell[2][1] == 2){
                        O()
                    }
                }
                Box(modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        if (playerCell[2][2] == 0) {
                            if (player1)
                                playerCell[2][2] = 1
                            else if (player2)
                                playerCell[2][2] = 2
                            onPlay()
                        }

                    }) {
                    if (playerCell[2][2] == 1){
                        X()
                    }
                    else if (playerCell[2][2] == 2){
                        O()
                    }
                }
            }
        }

    }

}

@Composable
fun X(){
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = Color.Black,
            start = Offset(10f, 10f),
            end = Offset(size.width-10f, size.height-10f),
            strokeWidth = 4f
        )
        drawLine(
            color = Color.Black,
            start = Offset(10f, size.height-10f),
            end = Offset(size.width-10f, 10f),
            strokeWidth = 4f
        )
    }
}

@Composable
fun O(){
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color.Black,
            center = Offset(size.width/2, size.height/2),
            radius = 60f,
            style = Stroke(width = 4f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameTablePreview() {
    TicTacToeGame()
}