package com.pokemon.pokemonbuilder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pokemon.pokemonbuilder.ui.theme.PokemonBuilderTheme
import com.pokemon.pokemonbuilder.ui.views.FirstLogin
import com.pokemon.pokemonbuilder.ui.views.LanguagePicker
import com.pokemon.pokemonbuilder.ui.views.SignUp
import com.pokemon.pokemonbuilder.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

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
                    LoginPage()
                }
            }
        }
    }
}

@Composable
fun LoginPage() {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "firstLogin"
    ){
        composable(route = "firstLogin"){
            FirstLogin(
                loginViewModel = loginViewModel,
                navController = navController
            )
        }
        composable(route = "pickLanguage"){
            LanguagePicker(
                loginViewModel = loginViewModel,
                navController = navController)
        }
        composable(route = "signUp"){
            SignUp(
                loginViewModel = loginViewModel,
                navController = navController
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PokemonBuilderTheme {

    }
}