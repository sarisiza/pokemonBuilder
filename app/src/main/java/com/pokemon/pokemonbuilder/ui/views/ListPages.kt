package com.pokemon.pokemonbuilder.ui.views

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
import com.pokemon.pokemonbuilder.viewmodel.BuilderViewModel
import com.pokemon.pokemonbuilder.viewmodel.DexViewModel
import com.pokemon.pokemonbuilder.viewmodel.LoginViewModel
import com.pokemon.pokemonbuilder.viewmodel.ViewIntents
import java.util.*

private const val TAG = "ListPages"

@Composable
fun PokemonInfo(
    dexViewModel: DexViewModel,
    loginViewModel: LoginViewModel,
    navController: NavController,
    windowSizeClass: WindowSizeClass
) {
    Column {
        val language = loginViewModel.appLanguage.value
        val generation = dexViewModel.selectedGeneration
        loginViewModel.getIntent(ViewIntents.GET_LANGUAGE)
        PokemonFilter(dexViewModel,windowSizeClass){gen->
            language?.let {lang->
                dexViewModel.getIntent(ViewIntents.GET_POKEMON(lang,gen))
            }
        }
        when(val state = dexViewModel.pokemonList.collectAsState(UIState.LOADING).value){
            is UIState.ERROR -> {
                ShowErrorDialog(e = state.e) {
                    language?.let {lang ->
                        dexViewModel.getIntent(ViewIntents.GET_POKEMON(lang,generation.generation.id))
                    }
                }
            }
            UIState.LOADING -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            is UIState.SUCCESS -> {
                PokemonList(
                    pokemonItems = state.response,
                    dexViewModel = dexViewModel,
                    windowSizeClass = windowSizeClass
                ){
                    dexViewModel.selectedPokemon = it
                    navController.navigate("pokemon_details")
                }
            }
        }
    }
}

@Composable
fun TeamsInfo(
    builderViewModel: BuilderViewModel,
    navController: NavController,
    windowSizeClass: WindowSizeClass
) {
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T>PokemonItemView(
    item: T,
    windowSizeClass: WindowSizeClass,
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
                val textSize =
                    when(windowSizeClass.widthSizeClass){
                        WindowWidthSizeClass.Compact -> 18.sp
                        WindowWidthSizeClass.Medium -> 20.sp
                        WindowWidthSizeClass.Expanded -> 22.sp
                        else -> 16.sp
                    }
                Row {
                    val pokemonImage = POKEMON_IMAGE_URL + item.id + ".png"
                    AsyncImage(
                        model =ImageRequest.Builder(LocalContext.current)
                            .data(pokemonImage)
                            .build(),
                        contentDescription = item.name,
                        contentScale = ContentScale.Fit
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
    dexViewModel: DexViewModel,
    windowSizeClass: WindowSizeClass,
    selectedItem: (T) -> Unit
){
    
    LazyColumn(
        state = rememberForeverLazyListState(key = dexViewModel.selectedGeneration.generation.region)
    ){
        itemsIndexed(items = pokemonItems){_,item ->
            PokemonItemView(
                item = item,
                windowSizeClass = windowSizeClass,
                selectedItem = selectedItem
            )
        }
    }
}

@Composable
fun PokemonFilter(
    dexViewModel: DexViewModel,
    windowSizeClass: WindowSizeClass,
    callPokemon: (Int) -> Unit
) {
    Row {
        val textSize =
            when(windowSizeClass.widthSizeClass){
                WindowWidthSizeClass.Compact -> 28.sp
                WindowWidthSizeClass.Medium -> 30.sp
                WindowWidthSizeClass.Expanded -> 32.sp
                else -> 26.sp
            }
        Text(
            text = stringResource(R.string.label_generation),
            fontSize = textSize,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterVertically)
        )
        GenerationSpinner(dexViewModel,windowSizeClass,callPokemon)
    }
}

@Composable
fun GenerationSpinner(
    dexViewModel: DexViewModel,
    windowSizeClass: WindowSizeClass,
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
    val textSize =
        when(windowSizeClass.widthSizeClass){
            WindowWidthSizeClass.Compact -> 16.sp
            WindowWidthSizeClass.Medium -> 18.sp
            WindowWidthSizeClass.Expanded -> 20.sp
            else -> 14.sp
        }
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