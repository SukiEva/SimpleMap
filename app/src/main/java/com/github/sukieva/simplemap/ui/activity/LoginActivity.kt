package com.github.sukieva.simplemap.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Interests
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.sukieva.simplemap.ui.activity.base.BaseActivity
import com.github.sukieva.simplemap.ui.activity.base.InitView
import com.github.sukieva.simplemap.ui.components.AnimatableSun
import com.github.sukieva.simplemap.ui.components.MaterialTopAppBar
import com.github.sukieva.simplemap.ui.components.MyScaffold
import com.github.sukieva.simplemap.ui.theme.White
import com.github.sukieva.simplemap.utils.ProvinceCityUtil
import com.github.sukieva.simplemap.utils.errorToast
import com.github.sukieva.simplemap.utils.getDate
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.listItemsMultiChoice
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch

@ExperimentalPagerApi
class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.getData()
        setContent {
            InitView {
                MyScaffold(topBar = { MaterialTopAppBar(title = "??????") }) {
                    LoginScreen()
                }
            }
        }
    }
}


@ExperimentalPagerApi
@Composable
fun LoginScreen(model: LoginViewModel = viewModel()) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val tabs = arrayOf("??????", "??????")
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        },
        backgroundColor = MaterialTheme.colors.background
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    // Animate to the selected page when clicked
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
    HorizontalPager(
        count = tabs.size,
        state = pagerState,
    ) {
        val account = remember { model.account }
        val password = remember { model.password }
        val remAccount = remember { model.remAccount }
        val remPassword = remember { model.remPassword }

        val accountRegister = remember { model.accountRegister }
        val passwordRegister = remember { model.passwordRegister }
        val passwordRepeatRegister = remember { model.passwordRepeatRegister }
        val sex = remember { model.sex } // true ??? false ???
        val birthday = remember { model.birthday }
        val phone = remember { model.phone }
        val email = remember { model.email }
        val interest = remember { model.interest }
        val description = remember { model.description }
        val locationProvince = remember { model.locationProvince }
        val locationMayor = remember { model.locationMayor }

        val accountValid = remember { model.accountValid }
        val passwordValid = remember { model.passwordValid }
        val phoneValid = remember { model.phoneValid }
        val emailValid = remember { model.emailValid }


        val dialogState = rememberMaterialDialogState()
        val interestDialogState = rememberMaterialDialogState()
        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                positiveButton("Ok")
                negativeButton("Cancel")
            }
        ) {
            datepicker(
                title = "?????????????????????",
                colors = DatePickerDefaults.colors(headerBackgroundColor = White)
            ) {
                model.birthday.value = it.toString()
                if (getDate() < birthday.value) {
                    "??????????????????".errorToast()
                }
            }
        }
        MaterialDialog(dialogState = interestDialogState, buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }) {
            listItemsMultiChoice(
                list = model.interests
            ) {
                var str = ""
                it.forEach { index ->
                    str += model.interests[index] + " "
                }
                model.interest.value = str
            }
        }

        when (pagerState.currentPage) {
            0 -> {
                Column(
                    Modifier
                        .fillMaxHeight()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatableSun(Modifier.size(150.dp))
                    MyTextField(state = account, label = "??????")
                    MyTextField(state = password, label = "??????", visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "????????????")
                        Checkbox(checked = remAccount.value, onCheckedChange = { remAccount.value = it })
                        Spacer(modifier = Modifier.width(30.dp))
                        Text(text = "????????????")
                        Checkbox(checked = remPassword.value, onCheckedChange = {
                            remPassword.value = it
                            if (it) remAccount.value = it
                        })
                    }
                    OutlinedButton(
                        onClick = { model.login() }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 40.dp)
                    ) {
                        Text(text = "??????")
                    }
                }
            }
            1 -> {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    LazyColumn(
                        Modifier
                            .fillMaxHeight()
                            .padding(top = 20.dp),
                        //horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            MyTextField(state = accountRegister, label = "??????", validState = accountValid)
                            MyTextField(state = passwordRegister, label = "??????", visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
                            MyTextField(state = passwordRepeatRegister, label = "????????????", visualTransformation = PasswordVisualTransformation(), validState = passwordValid, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
                            Row(
                                modifier = Modifier.padding(top = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "?????? : ")
                                Spacer(modifier = Modifier.width(20.dp))
                                RadioButton(selected = sex.value, onClick = { sex.value = true })
                                Text(text = "???")
                                Spacer(modifier = Modifier.width(20.dp))
                                RadioButton(selected = !sex.value, onClick = { sex.value = false })
                                Text(text = "???")
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 10.dp),
                            ) {
                                Text(text = "?????? : ")
                                Spacer(modifier = Modifier.width(20.dp))
                                OutlinedTextField(
                                    value = birthday.value,
                                    onValueChange = { birthday.value = it },
                                    trailingIcon = {
                                        Icon(Icons.Filled.Event, null,
                                            Modifier.clickable { dialogState.show() })
                                    },
                                    modifier = Modifier.width(150.dp)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 10.dp),
                            ) {
                                Text(text = "?????? : ")
                                Spacer(modifier = Modifier.width(20.dp))
                                DropDownField(state = locationProvince, label = "???")
                                Text(text = " - ")
                                DropDownField(state = locationMayor, label = "???", isProvince = false)
                            }
                            MyTextField(state = phone, label = "??????", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), validState = phoneValid)
                            MyTextField(state = email, label = "Email", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), validState = emailValid)
                            MyTextField(state = interest, label = "??????", trailingIcon = {
                                Icon(Icons.Filled.Interests, null,
                                    Modifier.clickable { interestDialogState.show() })
                            })
                            MyTextField(state = description, label = "????????????", modifier = Modifier.height(200.dp))
                            OutlinedButton(
                                onClick = { model.register() },
                                modifier = Modifier
                                    .width(300.dp)
                                    .padding(top = 40.dp, bottom = 40.dp)
                            ) {
                                Text(text = "??????")
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    state: MutableState<String>,
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isReadOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    validState: MutableState<Boolean> = mutableStateOf(false),
    model: LoginViewModel = viewModel(),
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = {
            state.value = it
            model.checkValid()
        },
        label = { Text(text = label) },
        visualTransformation = visualTransformation,
        readOnly = isReadOnly,
        isError = validState.value,
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .width(300.dp),
        trailingIcon = trailingIcon
    )
}

@Composable
fun DropDownField(
    state: MutableState<String>,
    label: String = "label",
    isProvince: Boolean = true,
    model: LoginViewModel = viewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    var textfieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    val icon = if (expanded)
        Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
    else
        Icons.Filled.ArrowDropDown
    val suggestions =
        if (isProvince) ProvinceCityUtil.PROVINCE_ARRAY
        else {
            val index = ProvinceCityUtil.PROVINCE_ARRAY.indexOf(
                model.locationProvince.value
            )
            if (index != -1) {
                ProvinceCityUtil.CITY_ARRAY[index]
            } else {
                arrayOf()
            }
        }
    Column {
        OutlinedTextField(
            value = state.value,
            onValueChange = { state.value = it },
            modifier = Modifier
                .width(120.dp)
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            label = { Text(label) },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    state.value = label
                    expanded = false
                    if (isProvince) model.locationMayor.value = ""
                }) {
                    Text(text = label)
                }
            }
        }
    }
}