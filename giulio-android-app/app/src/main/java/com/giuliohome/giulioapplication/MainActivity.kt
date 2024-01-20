package com.giuliohome.giulioapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity

import androidx.lifecycle.ViewModel


import com.giuliohome.giulioapplication.ui.theme.GiulioApplicationTheme

class SharedContentViewModel : ViewModel() {
    var sharedContent = mutableStateOf<String?>(null)
}



class MainActivity : ComponentActivity() {


    // Initialize the ViewModel using the by viewModels() delegate
    private val viewModel: SharedContentViewModel by viewModels()


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (intent != null) {
            val action = intent.action
            val type = intent.type

            if (Intent.ACTION_SEND == action && type != null) {
                if ("text/plain" == type) {
                    handleSharedText(intent, this) // Handle shared text
                } else {
                    viewModel.sharedContent.value = "unexpected $type type"
                }
            } else {
                viewModel.sharedContent.value = "unexpected $action action"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GiulioApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting(viewModel.sharedContent.value ?: "droid")
                }
            }
        }

        // Get the intent that started this activity
        val intent = intent
        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                handleSharedText(intent, this) // Handle shared text
            } else {
                viewModel.sharedContent.value = "onCreate unexpected $type type"
            }
        } else {
            viewModel.sharedContent.value = "onCreate unexpected $action action"
        }

    }

    private fun handleSharedText(intent: Intent, context: Context) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText != null) {
            // Update the shared content state
            viewModel.sharedContent.value = "open url: $sharedText"
            // Append a fixed part to the shared text
            val modifiedUrl = "https://my-app.cinemageddon2009.workers.dev/$sharedText"
            openUrlInBrowser(modifiedUrl, context)
        }
    }

    private fun openUrlInBrowser(url: String, context: Context) {
        // Create an intent to open the default browser
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        startActivity(context, browserIntent, null)
    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GiulioApplicationTheme {
        Greeting("cloud")
    }
}