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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pokemon.pokemonbuilder.PokemonQuery
import com.pokemon.pokemonbuilder.R
import com.pokemon.pokemonbuilder.domain.Pokemon
import com.pokemon.pokemonbuilder.domain.PokemonTeam
import com.pokemon.pokemonbuilder.utils.GenerationEnum
import com.pokemon.pokemonbuilder.utils.POKEMON_IMAGE_URL
import com.pokemon.pokemonbuilder.utils.QueryType
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
                    headerSize = headerSize
                ){
                    dexViewModel.selectedPokemon = it
                    navController.navigate(DexScreens.POKEMON_DETAILS.route)
                }
            }
        }
    }
}

@Composable
fun TeamsInfo(
    builderViewModel: BuilderViewModel,
    navController: NavController,
    headerSize: TextUnit
) {
    builderViewModel.getIntent(ViewIntents.VIEW_CREATED_TEAMS)
    Column {
        when(val states = builderViewModel.createdTeams.collectAsState().value){
            is UIState.ERROR -> {
                ShowErrorDialog(e = states.e) {
                    builderViewModel.getIntent(ViewIntents.VIEW_CREATED_TEAMS)
                }
            }
            UIState.LOADING -> {
                LoadingScreen()
            }
            is UIState.SUCCESS -> {
                PokemonList(
                    pokemonItems = states.response,
                    key = "teams-list",
                    headerSize = headerSize
                ){
                    builderViewModel.selectedTeam = it
                    navController.navigate(DexScreens.TEAMS_DETAIL.route)
                }
            }
        }
    }
}

@Composable
fun CreatedPokemonList(
    builderViewModel: BuilderViewModel,
    navController: NavController,
    headerSize: TextUnit,
    selectedTeam: PokemonTeam? = null
) {
    if(selectedTeam == null){
        builderViewModel.getIntent(ViewIntents.VIEW_CREATED_POKEMON)
    } else {
        builderViewModel.getIntent(ViewIntents.GET_POKEMON_IN_TEAM(selectedTeam))
    }
    Column {
        when(val state = builderViewModel.createdPokemon.collectAsState().value){
            is UIState.ERROR -> {
                ShowErrorDialog(e = state.e) {
                    if(selectedTeam == null){
                        builderViewModel.getIntent(ViewIntents.VIEW_CREATED_POKEMON)
                    } else {
                        builderViewModel.getIntent(ViewIntents.GET_POKEMON_IN_TEAM(selectedTeam))
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
                var key: String
                if(selectedTeam == null){
                    key = "Created Pokemon"
                } else {
                    key = selectedTeam.name
                }
                PokemonList(
                    pokemonItems = state.response,
                    key = key,
                    headerSize = headerSize
                ){
                    builderViewModel.selectedPokemon = it
                    navController.navigate(DexScreens.SAVED_POKEMON_DETAILS.route)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T>PokemonItemView(
    item: T,
    headerSize: TextUnit,
    selectedItem: (T) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = { selectedItem(item) }
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
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = item.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = headerSize,
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
            is PokemonTeam -> {
                Column {
                    Text(
                        text = item.name,
                        fontSize = headerSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun <T>PokemonList(
    pokemonItems: List<T>,
    key: String,
    headerSize: TextUnit,
    selectedItem: (T) -> Unit
){
    
    LazyColumn(
        state = rememberForeverLazyListState(key = key)
    ){
        itemsIndexed(items = pokemonItems){_,item ->
            PokemonItemView(
                item = item,
                headerSize = headerSize,
                selectedItem = selectedItem
            )
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