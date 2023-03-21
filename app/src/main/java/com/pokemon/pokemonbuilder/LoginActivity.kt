package com.pokemon.pokemonbuilder

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pokemon.pokemonbuilder.ui.theme.PokemonBuilderTheme
import com.pokemon.pokemonbuilder.ui.views.LanguagePicker
import com.pokemon.pokemonbuilder.ui.views.SignUp
import com.pokemon.pokemonbuilder.viewmodel.LoginViewModel
import com.pokemon.pokemonbuilder.viewmodel.ViewIntents
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LoginActivity"
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class LoginActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonBuilderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val loginViewModel = hiltViewModel<LoginViewModel>()
                    val navController = rememberNavController()
                    loginViewModel.getIntent(ViewIntents.CHECK_FIRST_TIME_LANGUAGE)
                    loginViewModel.getIntent(ViewIntents.CHECK_FIRST_TIME_USER)
                    val isFirstLanguage = loginViewModel.fistTimeLanguage.collectAsState()
                    val isFirstUser = loginViewModel.firstTimeUser.collectAsState()
                    val windowSizeClass = calculateWindowSizeClass(this)
                    val headerSize =
                        when(windowSizeClass.widthSizeClass){
                            WindowWidthSizeClass.Compact -> 28.sp
                            WindowWidthSizeClass.Medium -> 30.sp
                            WindowWidthSizeClass.Expanded -> 32.sp
                            else -> 20.sp
                        }
                    val titleSize =
                        when(windowSizeClass.widthSizeClass){
                            WindowWidthSizeClass.Compact -> 22.sp
                            WindowWidthSizeClass.Medium -> 24.sp
                            WindowWidthSizeClass.Expanded -> 26.sp
                            else -> 20.sp
                        }
                    val textSize =
                        when(windowSizeClass.widthSizeClass){
                            WindowWidthSizeClass.Compact -> 16.sp
                            WindowWidthSizeClass.Medium -> 18.sp
                            WindowWidthSizeClass.Expanded -> 20.sp
                            else -> 14.sp
                        }
                    NavHost(
                        navController = navController,
                        startDestination = "firstLogin"
                    ){
                        composable(route = "firstLogin"){
                            isFirstLanguage.value?.let {firstLanguage ->
                                if(!firstLanguage){
                                    navController.navigate("pickLanguage"){
                                        popUpTo(0)
                                    }
                                } else{
                                    isFirstUser.value?.let {firstUser ->
                                        if(!firstUser){
                                            navController.navigate("signUp"){
                                                popUpTo(0)
                                            }
                                        } else{
                                            navController.backQueue.clear()
                                            goToMainActivity()
                                        }
                                    }
                                }
                            }
                        }
                        composable(route = "pickLanguage"){
                            LanguagePicker(
                                loginViewModel = loginViewModel,
                                headerSize = headerSize,
                                titleSize = titleSize,
                                textSize = textSize
                            ){
                                isFirstUser.value?.let {firstUser ->
                                    if(!firstUser){
                                        navController.navigate("signUp")
                                    }
                                    else{
                                        goToMainActivity()
                                    }
                                }
                            }
                        }
                        composable(route = "signUp"){
                            SignUp(
                                loginViewModel = loginViewModel,
                                headerSize = headerSize,
                                titleSize = titleSize,
                                textSize = textSize
                            ){
                                navController.backQueue.clear()
                                goToMainActivity()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun goToMainActivity(){
        Intent(this,MainActivity::class.java).apply {
            startActivity(this)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PokemonBuilderTheme {
    }
}