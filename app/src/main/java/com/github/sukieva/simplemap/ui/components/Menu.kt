package com.github.sukieva.simplemap.ui.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.sukieva.simplemap.ui.activity.MainViewModel
import com.github.sukieva.simplemap.utils.ActivityCollector
import com.github.sukieva.simplemap.utils.alert
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.listItems
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

@Composable
fun SearchMenu(
    model: MainViewModel = viewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    val dialogState = rememberMaterialDialogState()
    model.getPersonalData()
    MaterialDialog(dialogState = dialogState) {
        title("个人信息")
        listItems(list = model.data)
    }
    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = null, modifier = Modifier.size(52.dp))
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                dialogState.show()
            }) {
                Text("当前用户信息")
            }
            DropdownMenuItem(onClick = {
                expanded = false
                alert(
                    title = "退出提醒                  ",
                    message = "你确定退出吗？",
                    onPositiveClick = { ActivityCollector.finishAllActivity() },
                    showNegative = true
                )
            }) {
                Text("退出")
            }
        }
    }
}
