package com.github.sukieva.simplemap.ui.activity.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.github.sukieva.simplemap.ui.theme.SimpleMapTheme
import com.github.sukieva.simplemap.utils.ActivityCollector

open class BaseActivity : ComponentActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

}


@Composable
fun InitView(content: @Composable () -> Unit) {

    SimpleMapTheme {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }

}