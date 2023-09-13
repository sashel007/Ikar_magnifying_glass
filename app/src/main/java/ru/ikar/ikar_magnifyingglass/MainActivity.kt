package ru.ikar.ikar_magnifyingglass

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import ru.ikar.ikar_magnifyingglass.ui.theme.IKAR_magnifyingGlassTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IKAR_magnifyingGlassTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        val context = LocalContext.current
                        val magnifyingView = MagnifyingView(context, null)

                        AndroidView(factory = { magnifyingView }, modifier = Modifier.height(300.dp)) { view ->
                            // ...
                        }

                        DimmingSeekBar(magnifyingView, modifier = Modifier.zIndex(1f))
                    }
                }
            }
        }
        this@MainActivity.moveTaskToBack(true)
    }
}

@Composable
fun DimmingSeekBar(magnifyingView: MagnifyingView, modifier: Modifier) {
    var sliderValue by remember { mutableStateOf(1f) } // начальное значение 1 для нормального масштаба

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Slider(
            value = sliderValue,
            onValueChange = {
                sliderValue = it
                magnifyingView.setScale(it) // устанавливаем масштаб для MagnifyingView
            },
            valueRange = 1f..3f, // допустимый диапазон масштабирования, например от 1x до 3x
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            ),
            modifier = Modifier.width(300.dp)
        )
    }
}

