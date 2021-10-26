package com.github.sukieva.simplemap.ui.activity


import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.github.sukieva.simplemap.R
import com.github.sukieva.simplemap.utils.DataManager
import com.github.sukieva.simplemap.utils.errorToast
import com.github.sukieva.simplemap.utils.warningToast
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    var mapView: MapView? = null
    var baiduMap: BaiduMap? = null

    var longitude = mutableStateOf("")
    var latitude = mutableStateOf("")

    var account = ""
    var sex = ""
    var birthday = ""
    var phone = ""
    var email = ""
    var interest = ""
    var description = ""
    var locationProvince = ""
    var locationMayor = ""
    var data = mutableListOf<String>()

    fun initView(context: Context) {
        mapView = MapView(context)
        baiduMap = mapView!!.map
        mapView?.apply {
            showZoomControls(false)
        }
        baiduMap?.apply {
            isMyLocationEnabled = true
        }
    }


    fun locateDialog() {
        if (longitude.value == "") {
            "经度不能为空".errorToast()
            return
        }
        if (latitude.value == "") {
            "纬度不能为空".errorToast()
            return
        }
        if (longitude.value.toDouble() < -180 || longitude.value.toDouble() > 180) {
            "经度的范围在-180~180之间!".warningToast()
            return
        }
        if (latitude.value.toDouble() < -90 || latitude.value.toDouble() > 90) {
            "纬度的范围在-90~90之间!".warningToast()
            return
        }
        // 定义Maker坐标点
        pointOverlay(longitude.value.toDouble(), latitude.value.toDouble())
        // 将地图移动过去
        toNewAddress(longitude.value.toDouble(), latitude.value.toDouble())
    }


    fun toNewAddress(longitude: Double, latitude: Double) {
        //设定中心点坐标
        val centerPoint = LatLng(latitude, longitude)
        //定义地图状态
        val mapStatus = MapStatus.Builder().target(centerPoint).build()
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        val mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus)
        //改变地图状态
        baiduMap!!.setMapStatus(mapStatusUpdate)
    }

    fun pointOverlay(longitude: Double, latitude: Double) {
        //定义Maker坐标点
        val point = LatLng(latitude, longitude)
        //构建Marker图标
        val bitmap = BitmapDescriptorFactory.fromResource(R.drawable.mark)
        //构建MarkerOption，用于在地图上添加Marker
        val option: OverlayOptions = MarkerOptions().position(point).icon(bitmap)
        //在地图上添加Marker，并显示
        baiduMap!!.addOverlay(option)
    }

    fun getPersonalData() {
        viewModelScope.launch {
            account = DataManager.readData("account", "")
            sex = if (DataManager.readData("sex", true)) "男" else "女"
            birthday = DataManager.readData("birthday", "")
            locationProvince = DataManager.readData("locationProvince", "")
            locationMayor = DataManager.readData("locationMayor", "")
            phone = DataManager.readData("phone", "")
            email = DataManager.readData("email", "")
            interest = DataManager.readData("interest", "")
            description = DataManager.readData("description", "")
            data = mutableListOf(
                "姓名 : $account",
                "性别 : $sex",
                "生日 : $birthday",
                "籍贯 : $locationProvince-$locationMayor",
                "手机 : $phone",
                "邮件 : $email",
                "兴趣 : $interest",
                "自我介绍 : $description"
            )
        }
    }
}