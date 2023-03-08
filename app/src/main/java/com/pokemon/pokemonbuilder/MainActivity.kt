package com.pokemon.pokemonbuilder

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.pokemon.pokemonbuilder.ui.theme.PokemonBuilderTheme
import com.pokemon.pokemonbuilder.ui.views.DexScreens
import com.pokemon.pokemonbuilder.ui.views.PokemonDetailsScreen
import com.pokemon.pokemonbuilder.ui.views.PokemonInfo
import com.pokemon.pokemonbuilder.ui.views.PokemonItemView
import com.pokemon.pokemonbuilder.viewmodel.DexViewModel
import com.pokemon.pokemonbuilder.viewmodel.ViewIntents
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonBuilderTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Pokemon Builder")
                            }
                        )
                    }
                ) {
                    val dexViewModel = hiltViewModel<DexViewModel>()
                    NavHost(
                        navController = navController,
                        modifier = Modifier.padding(it),
                        startDestination = "pokedex"
                    ){
                        navigation(
                            startDestination = "pokemon_list",
                            route = "pokedex"
                        ){
                            composable("pokemon_list"){
                                dexViewModel.getIntent(ViewIntents.GET_POKEMON(9))
                                PokemonInfo(
                                    dexViewModel = dexViewModel,
                                    navController = navController
                                )
                            }
                            composable("pokemon_details"){
                                dexViewModel.selectedPokemon?.let {
                                    PokemonDetailsScreen(selectedPokemon = it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    PokemonBuilderTheme {
    }
}