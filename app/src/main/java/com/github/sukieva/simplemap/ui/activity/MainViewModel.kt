package com.github.sukieva.simplemap.ui.activity


import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.github.sukieva.simplemap.R
import kotlinx.coroutines.launch
import com.baidu.mapapi.utils.DistanceUtil
import com.github.sukieva.simplemap.utils.*


class MainViewModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    var mapView: MapView? = null
    var baiduMap: BaiduMap? = null

    var longitude = mutableStateOf("")
    var latitude = mutableStateOf("")
    var citySearch = mutableStateOf("")
    var beginLongitude = mutableStateOf("")
    var beginLatitude = mutableStateOf("")
    var endLongitude = mutableStateOf("")
    var endLatitude = mutableStateOf("")

    private var account = ""
    private var sex = ""
    private var birthday = ""
    private var phone = ""
    private var email = ""
    private var interest = ""
    private var description = ""
    private var locationProvince = ""
    private var locationMayor = ""
    var data = mutableListOf<String>()

    private val cityPin = arrayOf("nanjing", "beijing", "shanghai", "suzhou", "yongzhou", "lishui")
    private val city = arrayOf("南京", "北京", "上海", "苏州", "永州", "丽水")
    private val cityLocate = arrayOf(
        arrayOf(118.78, 32.07),
        arrayOf(116.40, 39.90),
        arrayOf(121.48, 31.22),
        arrayOf(116.97, 33.63),
        arrayOf(111.608019, 26.434516),
        arrayOf(119.921786, 28.451993)
    )

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

    fun citySearchSubmit() {
        if (citySearch.value == "") {
            "城市不能为空".warningToast()
            return
        }
        val index = city.indexOf(citySearch.value)
        val indexPin = cityPin.indexOf(citySearch.value)
        val lo: Double
        val la: Double
        when {
            index != -1 -> {
                lo = cityLocate[index][0]
                la = cityLocate[index][1]
            }
            indexPin != -1 -> {
                lo = cityLocate[indexPin][0]
                la = cityLocate[indexPin][1]
            }
            else -> {
                "数据录入中，请稍后……".infoToast()
                return
            }
        }
        pointOverlay(lo, la)
        toNewAddress(lo, la)
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

    fun calculateDistance() {
        when {
            beginLongitude.value == "" -> "起点经度不能为空".warningToast()
            beginLatitude.value == "" -> "起点纬度不能为空".warningToast()
            endLongitude.value == "" -> "终点经度不能为空".warningToast()
            endLatitude.value == "" -> "终点纬度不能为空".warningToast()
            beginLongitude.value.toDouble() < -180 || beginLongitude.value.toDouble() > 180 ||
                    endLongitude.value.toDouble() < -180 || endLongitude.value.toDouble() > 180
            -> "经度的范围在-180~180之间！".errorToast()
            beginLatitude.value.toDouble() < -90 || beginLatitude.value.toDouble() > 90 ||
                    endLatitude.value.toDouble() < -90 || endLatitude.value.toDouble() > 90
            -> "纬度的范围在-90~90之间！".errorToast()
            else -> {
                val beginLatLng = LatLng(beginLatitude.value.toDouble(), beginLongitude.value.toDouble())
                val endLatLng = LatLng(endLatitude.value.toDouble(), endLongitude.value.toDouble())
                val distance = DistanceUtil.getDistance(beginLatLng, endLatLng) / 1000
                val distanceString = String.format("%.2f", distance)
                "两地相距 $distanceString 公里！".successToast()
            }
        }
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
                "账号: $account",
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