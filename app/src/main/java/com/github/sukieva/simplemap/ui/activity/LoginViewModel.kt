package com.github.sukieva.simplemap.ui.activity

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sukieva.simplemap.MainActivity
import com.github.sukieva.simplemap.utils.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.vanpra.composematerialdialogs.MaterialDialogState
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginViewModel : ViewModel() {

    val interests = listOf("游泳", "足球", "篮球","电影","编程","唱歌","跳舞","下棋")

    // 登录
    var account = mutableStateOf("")
    var password = mutableStateOf("")
    var remAccount = mutableStateOf(false)
    var remPassword = mutableStateOf(false)

    // 注册
    var accountRegister = mutableStateOf("")
    var passwordRegister = mutableStateOf("")
    var passwordRepeatRegister = mutableStateOf("")
    var sex = mutableStateOf(true) // true 男 false 女
    var birthday = mutableStateOf("")
    var phone = mutableStateOf("")
    var email = mutableStateOf("")
    var interest = mutableStateOf("")
    var description = mutableStateOf("")
    var locationProvince = mutableStateOf("")
    var locationMayor = mutableStateOf("")
    var savedAccount = ""
    var savedPassword = ""

    // 验证
    var accountValid = mutableStateOf(false)
    var passwordValid = mutableStateOf(false)
    var phoneValid = mutableStateOf(false)
    var emailValid = mutableStateOf(false)

    fun login() {
        viewModelScope.launch {
            DataManager.saveData("account", account.value)
            DataManager.saveData("password", password.value)
            DataManager.saveData("remAccount", remAccount.value)
            DataManager.saveData("remPassword", remPassword.value)
        }
        "登录成功".successToast()
        ActivityCollector.finishTopActivity()
    }


    fun register() {
        if (checkValid(true)) {
            saveRegisterData()
            "注册成功".successToast()
            ActivityCollector.finishTopActivity()
        }
    }

    fun checkValid(isSubmit: Boolean = false): Boolean {
        if (isSubmit && (accountRegister.value == "" || passwordRegister.value == "")) {
            "请补充完整信息".warningToast()
            return false
        }
        if (accountRegister.value == savedAccount) {
            accountValid.value = true
            if (isSubmit) "该账户已注册".errorToast()
            return false
        } else accountValid.value = false
        if (passwordRegister.value != passwordRepeatRegister.value) {
            passwordValid.value = true
            if (isSubmit) "两次输入密码不相同".errorToast()
            return false
        } else passwordValid.value = false
        if (isPhoneNum(phone.value)) {
            phoneValid.value = true
            if (isSubmit) "手机号码格式不正确".errorToast()
            return false
        } else phoneValid.value = false


        if (checkEmail()) {
            emailValid.value = true
            if (isSubmit) "邮箱格式不正确".errorToast()
            return false
        } else emailValid.value = false
        if (isSubmit && getDate() < birthday.value) {
            "生日超出范围".errorToast()
            return false
        }
        return true
    }

    fun getData() {
        viewModelScope.launch {
            savedAccount = DataManager.readData("account", "")
            savedPassword = DataManager.readData("password", "")
            remAccount.value = DataManager.readData("remAccount", false)
            remPassword.value = DataManager.readData("remPassword", false)
            if (remAccount.value) {
                account.value = savedAccount
            }
            if (remPassword.value)
                password.value = savedPassword
        }
    }

    private fun saveRegisterData() {
        viewModelScope.launch {
            DataManager.saveData("account", accountRegister.value)
            DataManager.saveData("password", passwordRegister.value)
            DataManager.saveData("sex", sex.value)
            DataManager.saveData("birthday", birthday.value)
            DataManager.saveData("locationProvince", locationProvince.value)
            DataManager.saveData("locationMayor", locationMayor.value)
            DataManager.saveData("phone", phone.value)
            DataManager.saveData("email", email.value)
            DataManager.saveData("interest", interest.value)
            DataManager.saveData("description", description.value)
        }
    }


    private fun isPhoneNum(phone: String): Boolean {
        val compile = Pattern.compile("^(13|14|15|16|17|18|19)\\d{9}$")
        val matcher = compile.matcher(phone)
        return !matcher.matches()
    }

    private fun checkEmail(): Boolean {
        val regExp = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"
        val p = Pattern.compile(regExp)
        val m = p.matcher(email.value)
        return !m.matches()
    }
}