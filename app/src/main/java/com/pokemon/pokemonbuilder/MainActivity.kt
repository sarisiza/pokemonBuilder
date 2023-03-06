package com.pokemon.pokemonbuilder

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pokemon.pokemonbuilder.ui.theme.PokemonBuilderTheme
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
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        modifier = Modifier.padding(it),
                        startDestination = "pokemon_list"
                    ){
                        composable("pokemon_list"){
                            dexViewModel.getIntent(ViewIntents.GET_POKEMON(9))
                            PokemonInfo(
                                dexViewModel = dexViewModel,
                                navController = navController
                            )
                        }
                        composable("items_list"){

                        }
                        composable("moves_list"){

                        }
                        composable("pokemon_details"){

                        }
                        composable("item_details"){

                        }
                        composable("move_details"){

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
        PokemonItemView(
            item = PokemonQuery.Pokemon_v2_pokemon(
                1,
                "Bulbasaur",
                listOf(),
                listOf(),
                listOf(),
                null,
                listOf()
            ),
            selectedItem = {})
    }
}