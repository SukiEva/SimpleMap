package com.github.sukieva.simplemap.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable


@Composable
fun MyScaffold(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = topBar,
        content = {
            Column {
                content()
            }
        },
        bottomBar = bottomBar
    )
}