package com.github.sukieva.simplemap.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun IconItem(
    icon: ImageVector = Icons.Outlined.CheckCircleOutline,
    text: String = "message",
    onClick: () -> Unit = {},
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null,
            Modifier
                .clickable { onClick() }
                .size(45.dp)
        )
        Text(
            text = text,
            fontSize = 13.sp
        )
    }
}

@Preview
@Composable
fun IconItemPreview() {
    IconItem()
}