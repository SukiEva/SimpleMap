package com.github.sukieva.simplemap

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.baidu.mapapi.map.BaiduMap
import com.github.sukieva.simplemap.ui.activity.LoginActivity
import com.github.sukieva.simplemap.ui.activity.MainViewModel
import com.github.sukieva.simplemap.ui.activity.base.BaseActivity
import com.github.sukieva.simplemap.ui.activity.base.InitView
import com.github.sukieva.simplemap.ui.components.IconItem
import com.github.sukieva.simplemap.ui.components.SearchMenu
import com.github.sukieva.simplemap.ui.components.SplashScreen
import com.github.sukieva.simplemap.utils.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.input
import com.vanpra.composematerialdialogs.rememberMaterialDialogState


@ExperimentalPagerApi
@ExperimentalMaterialApi
class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.initView(this)
        viewModel.toNewAddress(118.78, 32.07)
        viewModel.pointOverlay(118.78, 32.07)
        setContent {
            val (appState, setAppState) = remember { mutableStateOf(AppState.Splash) }

            when (appState) {
                AppState.Splash -> {
                    SplashScreen { setAppState(AppState.Home) }
                }
                AppState.Home -> {
                    MainScreen()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.mapView?.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            alert(
                title = "退出提醒                  ",
                message = "你确定退出吗？",
                onPositiveClick = { ActivityCollector.finishAllActivity() },
                showNegative = true
            )
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }
}

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun MainScreen() {
    InitView {
        MainContent()
    }
}

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
private fun MainContent(
    model: MainViewModel = viewModel()
) {
    val locateDialogState = rememberMaterialDialogState()
    val longitude = remember { model.longitude }
    val latitude = remember { model.latitude }
    MaterialDialog(dialogState = locateDialogState, buttons = {
        positiveButton("Ok") {
            model.locateDialog()
        }
        negativeButton("Cancel") {
            longitude.value = ""
            latitude.value = ""
        }
    }) {
        Column(
            modifier = Modifier.padding(all = 20.dp),
        ) {
            Text(text = "经纬度定位")
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = longitude.value, onValueChange = { longitude.value = it }, label = { Text(text = "经度") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            OutlinedTextField(value = latitude.value, onValueChange = { latitude.value = it }, label = { Text(text = "纬度") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        }
    }

    BackdropScaffold(
        appBar = { },
        backLayerContent = {
            AndroidView(
                factory = {
                    model.mapView!!
                }
            )
        },
        frontLayerContent = {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp)
                ) {
                    Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = null, modifier = Modifier
                        .size(52.dp)
                        .clickable { start<LoginActivity>() })
                    OutlinedTextField(
                        value = "", onValueChange = {},
                        modifier = Modifier.padding(start = 10.dp),
                        placeholder = { Text(text = "搜索、上下滑动") },
                    )
                    SearchMenu()
                }
                Divider()
                Row(Modifier.padding(all = 10.dp)) {
                    IconItem(text = "空白地图",
                        icon = Icons.Outlined.CheckBoxOutlineBlank,
                        onClick = {
                            model.baiduMap?.mapType = BaiduMap.MAP_TYPE_NONE
                            "切换空白地图".infoToast()
                        })
                    Spacer(modifier = Modifier.size(25.dp))
                    IconItem(text = "普通地图",
                        icon = Icons.Outlined.Map,
                        onClick = {
                            model.baiduMap?.mapType = BaiduMap.MAP_TYPE_NORMAL
                            "切换普通地图".infoToast()
                        })
                    Spacer(modifier = Modifier.size(25.dp))
                    IconItem(text = "卫星地图",
                        icon = Icons.Outlined.Satellite,
                        onClick = {
                            model.baiduMap?.mapType = BaiduMap.MAP_TYPE_SATELLITE
                            "切换卫星地图".infoToast()
                        })
                    Spacer(modifier = Modifier.size(25.dp))
                    IconItem(text = "交通地图",
                        icon = Icons.Outlined.Traffic,
                        onClick = {
                            model.baiduMap?.let {
                                if (!it.isTrafficEnabled)
                                    "开启交通地图".successToast()
                                else
                                    "关闭交通地图".infoToast()
                                it.isTrafficEnabled = !it.isTrafficEnabled
                            }
                        })
                    Spacer(modifier = Modifier.size(25.dp))
                    IconItem(text = "热力地图",
                        icon = Icons.Outlined.LocalFireDepartment,
                        onClick = {
                            model.baiduMap?.let {
                                if (!it.isBaiduHeatMapEnabled)
                                    "开启热力地图".successToast()
                                else
                                    "关闭热力地图".infoToast()
                                it.isBaiduHeatMapEnabled = !it.isBaiduHeatMapEnabled
                            }
                        })
                }

                Row(Modifier.padding(all = 10.dp)) {
                    IconItem(text = "经纬定位",
                        icon = Icons.Outlined.PinDrop,
                        onClick = {
                            locateDialogState.show()
                        })
                    Spacer(modifier = Modifier.size(25.dp))
                    IconItem(text = "城市定位",
                        icon = Icons.Outlined.LocationCity,
                        onClick = {

                        })
                    Spacer(modifier = Modifier.size(25.dp))
                    IconItem(text = "公里计算",
                        icon = Icons.Outlined.Calculate,
                        onClick = {

                        })
                    Spacer(modifier = Modifier.size(25.dp))
                }
            }
        },
        gesturesEnabled = true,
        peekHeight = 450.dp,
        headerHeight = 70.dp,
        backLayerBackgroundColor = Color.Transparent
    )

}


enum class AppState {
    Splash,
    Home
}






