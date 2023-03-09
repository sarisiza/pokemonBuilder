package com.pokemon.pokemonbuilder.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pokemon.pokemonbuilder.PokemonQuery
import com.pokemon.pokemonbuilder.R
import com.pokemon.pokemonbuilder.utils.GenerationEnum
import com.pokemon.pokemonbuilder.utils.POKEMON_IMAGE_URL
import com.pokemon.pokemonbuilder.utils.UIState
import com.pokemon.pokemonbuilder.viewmodel.DexViewModel
import com.pokemon.pokemonbuilder.viewmodel.LoginViewModel
import com.pokemon.pokemonbuilder.viewmodel.ViewIntents
import java.util.*

private const val TAG = "ListPages"

@Composable
fun PokemonInfo(
    dexViewModel: DexViewModel,
    loginViewModel: LoginViewModel,
    navController: NavController
) {
    Column {
        val language = loginViewModel.appLanguage.value
        loginViewModel.getIntent(ViewIntents.GET_LANGUAGE)
        PokemonFilter(dexViewModel){generation->
            language?.let {lang->
                dexViewModel.getIntent(ViewIntents.GET_POKEMON(lang,generation))
            }
        }
        when(val state = dexViewModel.pokemonList.collectAsState(UIState.LOADING).value){
            is UIState.ERROR -> {}
            UIState.LOADING -> {
                CircularProgressIndicator()
            }
            is UIState.SUCCESS -> {
                PokemonList(
                    pokemonItems = state.response
                ){
                    dexViewModel.selectedPokemon = it
                    navController.navigate("pokemon_details")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T>PokemonItemView(
    item: T,
    selectedItem: (T) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = { selectedItem.invoke(item) }
    ) {
        when(item){
            is PokemonQuery.Pokemon_v2_pokemon -> {
                Row {
                    val pokemonImage = POKEMON_IMAGE_URL + item.id + ".png"
                    AsyncImage(
                        model =ImageRequest.Builder(LocalContext.current)
                            .data(pokemonImage)
                            .build(),
                        contentDescription = item.name,
                        contentScale = ContentScale.None
                    )
                    Text(
                        text = item.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@Composable
fun <T>PokemonList(
    pokemonItems: List<T>,
    selectedItem: (T) -> Unit
){
    LazyColumn{
        itemsIndexed(items = pokemonItems){_,item ->
            PokemonItemView(
                item = item,
                selectedItem = selectedItem
            )
        }
    }
}

@Composable
fun PokemonFilter(
    dexViewModel: DexViewModel,
    callPokemon: (Int) -> Unit
) {
    Row {
        Text(
            text = stringResource(R.string.label_generation),
            fontSize = 30.sp,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterVertically)
        )
        GenerationSpinner(dexViewModel,callPokemon)
    }
}

@Composable
fun GenerationSpinner(
    dexViewModel: DexViewModel,
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
            }
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