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
import com.pokemon.pokemonbuilder.domain.PokemonTeam
import com.pokemon.pokemonbuilder.utils.GenerationEnum
import com.pokemon.pokemonbuilder.utils.QueryType
import com.pokemon.pokemonbuilder.utils.UIState
import com.pokemon.pokemonbuilder.viewmodel.BuilderViewModel
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
                                generation = gen
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
    callPokemon: (Int) -> Unit
) {
    Row {
        Text(
            text = stringResource(R.string.label_generation),
            fontSize = headerSize,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterVertically)
        )
        GenerationSpinner(dexViewModel,textSize,callPokemon)
    }
}

@Composable
fun GenerationSpinner(
    dexViewModel: DexViewModel,
    textSize: TextUnit,
    callPokemon: (Int) -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    val generations = GenerationEnum.values()
    var selectedGeneration by remember { mutableStateOf(dexViewModel.selectedGeneration) }
    var selectedGenerationText by remember { mutableStateOf(selectedGeneration.generation.region) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if(expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        OutlinedTextField(
            value = selectedGenerationText,
            enabled = false,
            onValueChange = {selectedGenerationText = it},
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            label = {Text(stringResource(R.string.label_generation))},
            trailingIcon = {
                Icon(
                    icon,
                    stringResource(R.string.label_pick_generation),
                    Modifier.clickable { expanded = !expanded }
                )
            },
            textStyle = TextStyle(
                fontSize = textSize
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            generations.forEach {generation ->
                DropdownMenuItem(onClick = {
                    selectedGeneration = generation
                    selectedGenerationText = generation.generation.region
                    dexViewModel.selectedGeneration = selectedGeneration
                    expanded = false
                    callPokemon.invoke(selectedGeneration.generation.id)
                }) {
                    Text(text = generation.generation.region)
                }
            }
        }
    }
}