package ru.ikar.ikar_magnifyingglass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import ru.ikar.ikar_magnifyingglass.ui.theme.IKAR_magnifyingGlassTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IKAR_magnifyingGlassTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AndroidView(factory = { context ->
                        MagnifyingView(context, null)  // Creating the MagnifyingView without AttributeSet
                    }, modifier = Modifier.fillMaxSize()) { view ->
                        // You can update your view properties here if required
                    }
                }
            }
        }
        this@MainActivity.moveTaskToBack(true)
    }
}
