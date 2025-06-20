package com.example.candroid

import android.os.Bundle
import android.content.Intent
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.candroid.ui.theme.candroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            candroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Text(
        text = "Main act",
        modifier = modifier
    )
    Button(
        onClick = {
            val intent = Intent(context, Game::class.java)
            context.startActivity(intent)
        },
        modifier = modifier
    ){ Text("Go to game") }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    candroidTheme {
        MainScreen()
    }
}