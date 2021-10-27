package com.github.sukieva.simplemap.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.sukieva.simplemap.R
import com.github.sukieva.simplemap.ui.activity.base.InitView
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(onSplashCompleted: () -> Unit) {
    InitView {
        Surface(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .padding(16.dp)
            ) {
                LaunchedEffect(Unit) {
                    delay(1000)
                    onSplashCompleted()
                }
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.ic_launcher_round),
                        contentDescription = "Logo",
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(text = "导航专业")
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(text = "数据准确")
                }
            }
        }
    }
}





