package com.pokemon.pokemonbuilder.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.pokemon.pokemonbuilder.R
import com.pokemon.pokemonbuilder.utils.GenerationEnum
import com.pokemon.pokemonbuilder.utils.QueryType
import com.pokemon.pokemonbuilder.utils.UIState
import com.pokemon.pokemonbuilder.viewmodel.DexViewModel
import com.pokemon.pokemonbuilder.viewmodel.LoginViewModel
import com.pokemon.pokemonbuilder.viewmodel.ViewIntents

private const val TAG = "ListPages"

@Composable
fun PokemonInfo(
    dexViewModel: DexViewModel,
    loginViewModel: LoginViewModel,
    navController: NavController,
    headerSize: TextUnit,
    textSize: TextUnit,
    queryType: QueryType
) {
    Column {
        val language = loginViewModel.appLanguage.value
        loginViewModel.getIntent(ViewIntents.GET_LANGUAGE)
        language?.let {lang ->
            when(queryType){
                QueryType.GENERATION -> {
                    PokemonFilter(dexViewModel,headerSize,textSize){gen->
                        dexViewModel.getIntent(
                            ViewIntents.GET_POKEMON(
                                language = lang,
                                generation = gen.generation.id
                            )
                        )
                    }
                }
                QueryType.TYPE -> {
                    dexViewModel.getIntent(
                        ViewIntents.GET_POKEMON(
                            language = lang,
                            type = dexViewModel.selectedType
                        )
                    )
                }
                is QueryType.NAME -> {
                    if(queryType.search) {
                        dexViewModel.getIntent(
                            ViewIntents.GET_POKEMON(
                                language = lang,
                                pokemonName = dexViewModel.searchForName
                            )
                        )
                    }
                }
            }
        }
        when(val state = dexViewModel.pokemonList.collectAsState(UIState.LOADING).value){
            is UIState.ERROR -> {
                ShowErrorDialog(e = state.e) {
                    language?.let {lang ->
                        when(queryType){
                            QueryType.GENERATION -> {
                                dexViewModel.getIntent(
                                    ViewIntents.GET_POKEMON(
                                        language = lang,
                                        generation = dexViewModel.selectedGeneration.generation.id
                                    )
                                )
                            }
                            QueryType.TYPE -> {
                                dexViewModel.getIntent(
                                    ViewIntents.GET_POKEMON(
                                        language = lang,
                                        type = dexViewModel.selectedType
                                    )
                                )
                            }
                            is QueryType.NAME -> {
                                if(queryType.search) {
                                    dexViewModel.getIntent(
                                        ViewIntents.GET_POKEMON(
                                            language = lang,
                                            pokemonName = dexViewModel.searchForName
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            UIState.LOADING -> {
                LoadingScreen()
            }
            is UIState.SUCCESS -> {
                PokemonList(
                    pokemonItems = state.response,
                    key = dexViewModel.selectedGeneration.generation.region,
                    headerSize = headerSize,
                    selectedItem = {
                        dexViewModel.selectedPokemon = it
                        navController.navigate(DexScreens.POKEMON_DETAILS.route)
                    }
                )
            }
        }
    }
}

@Composable
fun PokemonFilter(
    dexViewModel: DexViewModel,
    headerSize: TextUnit,
    textSize: TextUnit,
    callPokemon: (GenerationEnum) -> Unit
) {
    Row {
        Text(
            text = stringResource(R.string.label_generation),
            fontSize = headerSize,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterVertically)
        )
        PokemonSpinner(
            itemSet = GenerationEnum.values().toList(),
            textSize = textSize,
            selected = dexViewModel.selectedGeneration,
            getItem = callPokemon
        )
    }
}