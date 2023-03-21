package com.pokemon.pokemonbuilder.ui.views

import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.pokemon.pokemonbuilder.R
import com.pokemon.pokemonbuilder.utils.LanguageEnum
import com.pokemon.pokemonbuilder.utils.User
import com.pokemon.pokemonbuilder.viewmodel.DexViewModel
import com.pokemon.pokemonbuilder.viewmodel.LoginViewModel
import com.pokemon.pokemonbuilder.viewmodel.ViewIntents

private const val TAG = "FirstLogin"

@Composable
fun LanguagePicker(
    loginViewModel: LoginViewModel,
    headerSize: TextUnit,
    titleSize: TextUnit,
    textSize: TextUnit,
    navigate: () -> Unit
) {
    var selectedLanguage by remember { mutableStateOf(LanguageEnum.ENG) }
    loginViewModel.getIntent(ViewIntents.GET_LANGUAGE)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.login_welcome),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
            fontSize = headerSize,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.login_pick_language),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
            fontSize = titleSize
        )
        PokemonSpinner(
            itemSet = LanguageEnum.values().toList(),
            textSize = textSize,
            selected = loginViewModel.appLanguage
        ){
            selectedLanguage = it as LanguageEnum
        }
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            onClick = {
                Log.d(TAG, "LanguagePicker: $selectedLanguage")
                loginViewModel.getIntent(ViewIntents.PICK_LANGUAGE(selectedLanguage))
                navigate()
            }
        ) {
            Text(text = stringResource(R.string.button_accept))
        }
    }
}

@Composable
fun SignUp(
    loginViewModel: LoginViewModel,
    headerSize: TextUnit,
    titleSize: TextUnit,
    textSize: TextUnit,
    mainPage: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        val maxChar = 10
        val focusManager = LocalFocusManager.current
        Text(
            text = stringResource(R.string.login_welcome),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
            fontSize = headerSize,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.login_sign_up),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
            fontSize = titleSize
        )
        Spacer(modifier = Modifier.size(20.dp))
        Row() {
            Text(
                text = stringResource(R.string.login_first_name),
                modifier = Modifier
                    .weight(20F)
                    .align(Alignment.CenterVertically)
                    .padding(5.dp),
            )
            OutlinedTextField(
                value = firstName,
                onValueChange = {
                    firstName = it.take(maxChar)
                    if(it.length>maxChar) focusManager.moveFocus(FocusDirection.Down)
                },
                label = { Text(text = stringResource(R.string.login_enter_first_name))},
                enabled = true,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = textSize
                ),
                modifier = Modifier
                    .weight(60F)
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                            focusManager.moveFocus(FocusDirection.Down)
                            true
                        }
                        false
                    }
            )
        }
        Row() {
            Text(
                text = stringResource(R.string.login_last_name),
                modifier = Modifier
                    .weight(20F)
                    .align(Alignment.CenterVertically)
                    .padding(5.dp)
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = {
                    lastName = it.take(maxChar)
                    if(it.length>maxChar) focusManager.clearFocus()
                },
                label = { Text(text = stringResource(R.string.login_enter_last_name))},
                enabled = true,
                singleLine = true,
                modifier = Modifier
                    .weight(60F)
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                            focusManager.clearFocus()
                            true
                        }
                        false
                    }
            )
        }
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            onClick = {
            val user = User(firstName,lastName)
            loginViewModel.getIntent(ViewIntents.SIGN_UP(user))
            mainPage.invoke()
        }) {
            Text(text = stringResource(R.string.button_sign_up))
        }
    }
}