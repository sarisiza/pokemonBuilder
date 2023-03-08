package com.pokemon.pokemonbuilder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                    NavHost(
                        navController = navController,
                        startDestination = "firstLogin"
                    ){
                        composable(route = "firstLogin"){
                            isFirstLanguage.value?.let {firstLanguage ->
                                if(!firstLanguage){
                                    navController.navigate("pickLanguage")
                                } else{
                                    isFirstUser.value?.let {firstUser ->
                                        if(!firstUser){
                                            navController.navigate("signUp")
                                        } else{
                                            Log.d(TAG, "FirstLogin: gets here")
                                            goToMainActivity()
                                        }
                                    }
                                }
                            }
                        }
                        composable(route = "pickLanguage"){
                            LanguagePicker(
                                loginViewModel = loginViewModel,
                                navController = navController
                            ) {
                                goToMainActivity()
                            }
                        }
                        composable(route = "signUp"){
                            SignUp(
                                loginViewModel = loginViewModel
                            ){
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