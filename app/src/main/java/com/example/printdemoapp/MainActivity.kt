package com.example.printdemoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.printdemoapp.ui.theme.PrintDemoAppTheme

val viewModel = PrintViewModel()
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrintDemoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        PrintTextView()
                    }
                }
            }
        }
    }
}

@Composable
fun PrintTextView() {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.End) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.printString,
            onValueChange = { viewModel.updatePrintString(it) },
            label = { Text(stringResource(R.string.print_input_placeholder)) }
        )
        Button(onClick = {
            viewModel.printStringToPrinter(context, viewModel.printString, "printTextJob")
        }) {
            Text(text = stringResource(R.string.print_text))
        }
    }
}
