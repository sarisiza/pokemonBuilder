package com.pokemon.pokemonbuilder.ui.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pokemon.pokemonbuilder.*
import com.pokemon.pokemonbuilder.R
import com.pokemon.pokemonbuilder.domain.Pokemon
import com.pokemon.pokemonbuilder.domain.PokemonTeam
import com.pokemon.pokemonbuilder.utils.*
import java.util.*

@Composable
fun ShowErrorDialog(
    e: Exception,
    retry: () -> Unit
) {
    val openDialog = remember { mutableStateOf(true) }
    if(openDialog.value){
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = stringResource(R.string.alert_title)) },
            text = {Text(text = e.localizedMessage?: stringResource(R.string.alert_unexpected))},
            dismissButton = {
                Button(onClick = {openDialog.value = false}) {
                    Text(text = stringResource(R.string.alert_dismiss))
                }
            },
            confirmButton = {
                Button(onClick = { retry() }) {
                    Text(text = stringResource(R.string.alert_retry))
                }
            }
        )
    }
}

@Composable
fun LoadingScreen() {
    Column {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T>PokemonList(
    pokemonItems: List<T>,
    key: String,
    headerSize: TextUnit? = null,
    titleSize: TextUnit? = null,
    textSize: TextUnit? = null,
    selectedItem: (T) -> Unit,
    swipeRight: ((T) -> Unit)? = null,
    swipeLeft: ((T) -> Unit)? = null
){
    val items: MutableList<T> = pokemonItems.toMutableList()
    LazyColumn(
        state = rememberForeverLazyListState(key = key)
    ){
        val directions: MutableSet<DismissDirection> = mutableSetOf()
        if(swipeLeft!=null) directions.add(DismissDirection.EndToStart)
        if(swipeRight!=null) directions.add(DismissDirection.StartToEnd)
        itemsIndexed(items = items){_,item ->
            val dismissState = rememberDismissState()
            if(dismissState.isDismissed(DismissDirection.EndToStart)){
                swipeLeft?.invoke(item)
            }
            if(dismissState.isDismissed(DismissDirection.StartToEnd)){
                swipeRight?.invoke(item)
            }
            if(dismissState.currentValue != DismissValue.Default){
                LaunchedEffect(Unit){
                    dismissState.reset()
                }
            }
            SwipeToDismiss(
                state = dismissState,
                background = {
                    val color by animateColorAsState(
                        targetValue = when(dismissState.targetValue) {
                            DismissValue.Default ->  Color.Transparent
                            DismissValue.DismissedToEnd -> Color.Blue
                            DismissValue.DismissedToStart  -> Color.Red

                        }
                    )
                    val alignment = if(dismissState.dismissDirection == DismissDirection.EndToStart)
                        Alignment.CenterEnd
                    else
                        Alignment.CenterStart
                    val icon =  when(dismissState.dismissDirection){
                        DismissDirection.StartToEnd -> Icons.Default.Edit
                        DismissDirection.EndToStart -> Icons.Default.Delete
                        null -> null
                    }
                    val scale by animateFloatAsState(
                        targetValue = if(dismissState.targetValue == DismissValue.Default) 0.75F else 1F
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 20.dp),
                    ){
                        icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                modifier = Modifier.align(alignment)
                            )
                        }
                    }
                },
                directions = directions
            ) {
                PokemonItemView(
                    item = item,
                    headerSize = headerSize,
                    titleSize = titleSize,
                    textSize = textSize,
                    selectedItem = selectedItem
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T>PokemonItemView(
    item: T,
    headerSize: TextUnit? = null,
    titleSize: TextUnit? = null,
    textSize: TextUnit? = null,
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
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(pokemonImage)
                            .build(),
                        contentDescription = item.name,
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = item.name.replaceFirstCap(),
                        fontWeight = FontWeight.Bold,
                        fontSize = headerSize?:20.sp,
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
            is PokemonTeam -> {
                Column {
                    Text(
                        text = item.name.replaceFirstCap(),
                        fontSize = headerSize?:20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
            is Pokemon -> {
                Row {
                    val pokemonImage = POKEMON_IMAGE_URL + item.pokemonId + ".png"
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(pokemonImage)
                            .build(),
                        contentDescription = item.name,
                        contentScale = ContentScale.Fit
                    )
                    Column(
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = item.nickname,
                            fontWeight = FontWeight.Bold,
                            fontSize = titleSize?:20.sp,
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = item.name.replaceFirstCap(),
                            fontSize = textSize?:18.sp,
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun <T>PokemonSpinner(
    itemSet: List<T>,
    textSize: TextUnit,
    selected: T? = null,
    getItem: (T) -> Unit
){
    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        var expanded by remember { mutableStateOf(false) }
        var textFieldSize by remember { mutableStateOf(Size.Zero) }
        val context = LocalContext.current
        val icon = if(expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown
        var selectedItem by remember {mutableStateOf(selected?:itemSet[0])}
        var selectedText by remember {
            when(selected){
                is NaturesQuery.Pokemon_v2_nature -> mutableStateOf(selected.pokemon_v2_naturenames[0].name)
                is TypesQuery.Pokemon_v2_type -> mutableStateOf(selected.pokemon_v2_typenames[0].name)
                is ItemsQuery.Pokemon_v2_item -> mutableStateOf(selected.pokemon_v2_itemnames[0].name)
                is GenerationEnum -> mutableStateOf(selected.generation.region)
                is GenderEnum -> mutableStateOf(context.getString(selected.gender))
                is LanguageEnum -> mutableStateOf(selected.language.name)
                else -> mutableStateOf("")
            }
        }
        OutlinedTextField(
            value = selectedText,
            enabled = false,
            onValueChange = {selectedText = it},
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            trailingIcon = {
                Icon(
                    icon,
                    stringResource(R.string.label_pick_icon),
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
            itemSet.forEach {item ->
                DropdownMenuItem(onClick = {
                    selectedItem = item
                    selectedText = when(item){
                        is NaturesQuery.Pokemon_v2_nature -> item.pokemon_v2_naturenames[0].name
                        is TypesQuery.Pokemon_v2_type -> item.pokemon_v2_typenames[0].name
                        is ItemsQuery.Pokemon_v2_item -> item.pokemon_v2_itemnames[0].name
                        is GenerationEnum -> item.generation.region
                        is GenderEnum -> context.getString(item.gender)
                        is LanguageEnum -> item.language.name
                        else -> ""
                    }
                    getItem(item)
                    expanded = false
                }) {
                    when(item){
                        is NaturesQuery.Pokemon_v2_nature -> {
                            Text(text = item.pokemon_v2_naturenames[0].name)
                        }
                        is TypesQuery.Pokemon_v2_type -> {
                            val typeImg = context.resIdByName(item.name,"drawable")
                            Image(
                                painter = painterResource(id = typeImg),
                                contentDescription = item.name,
                                modifier = Modifier
                                    .weight(0.2F)
                                    .clip(CircleShape)
                                    .padding(5.dp)
                            )
                            Text(text = item.pokemon_v2_typenames[0].name)
                        }
                        is ItemsQuery.Pokemon_v2_item -> {
                            val itemImg = "$ITEM_IMAGE_URL${item.name}.png"
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(itemImg)
                                    .build(),
                                contentDescription = item.name,
                                contentScale = ContentScale.None,
                            )
                            Text(text = item.pokemon_v2_itemnames[0].name)
                        }
                        is GenerationEnum -> {
                            Text(text = item.generation.region)
                        }
                        is GenderEnum -> {
                            Text(text = stringResource(id = item.gender))
                        }
                        is LanguageEnum -> {
                            Text(text = item.language.name)
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}
