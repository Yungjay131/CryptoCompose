package com.slyworks.cryptocompose.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.slyworks.cryptocompose.ui.activities.ui.theme.CryptoComposeTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CryptoComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background) {
                }
            }
        }
    }
}

@Composable
fun RegisterMain(){}

@Composable
fun LoginMain(){}

@Composable
fun LoginPage0(){}

@Composable
fun LoginPage1(){}

@Composable
fun LoginPage2(){}

@Composable
fun LoginPage3(){}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CryptoComposeTheme { }
}