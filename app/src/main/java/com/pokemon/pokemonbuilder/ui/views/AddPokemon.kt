@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.pokemon.pokemonbuilder.ui.views

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pokemon.pokemonbuilder.*
import com.pokemon.pokemonbuilder.R
import com.pokemon.pokemonbuilder.domain.Pokemon
import com.pokemon.pokemonbuilder.domain.mapToCreatedPokemon
import com.pokemon.pokemonbuilder.utils.*
import com.pokemon.pokemonbuilder.viewmodel.BuilderViewModel
import com.pokemon.pokemonbuilder.viewmodel.DexViewModel
import com.pokemon.pokemonbuilder.viewmodel.LoginViewModel
import com.pokemon.pokemonbuilder.viewmodel.ViewIntents

@Composable
fun CreatePokemon(
    dexViewModel: DexViewModel,
    builderViewModel: BuilderViewModel,
    loginViewModel: LoginViewModel,
    headerSize: TextUnit,
    titleSize: TextUnit,
    textSize: TextUnit,
    action: DatabaseAction,
    inTeam: Boolean,
    navController: NavHostController,
    savedPokemon: Pokemon? = null
) {
    val natureState = dexViewModel.naturesList.collectAsState()
    val typeState = dexViewModel.typesList.collectAsState()
    val itemState = dexViewModel.itemsList.collectAsState()
    val languageState = loginViewModel.appLanguage.value
    loginViewModel.getIntent(ViewIntents.GET_LANGUAGE)
    val createdPokemon by remember {
        mutableStateOf(savedPokemon ?: dexViewModel.selectedPokemon.mapToCreatedPokemon())
    }
    createdPokemon.inTeam = inTeam
    languageState?.let {language->
        dexViewModel.getIntent(ViewIntents.GET_NATURES(language))
        dexViewModel.getIntent(ViewIntents.GET_TYPES(language))
        dexViewModel.getIntent(ViewIntents.GET_ITEMS(language))
    }
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.padding(10.dp)
    ){
        item{
            PokemonNameCard(
                createdPokemon = createdPokemon,
                textSize = headerSize
            )
        }
        item {
            PokemonNicknameCard(
                titleSize = titleSize,
                textSize = textSize,
                createdPokemon = createdPokemon
            )
        }
        item {
            PokemonLevelCard(
                titleSize = titleSize,
                textSize = textSize,
                createdPokemon = createdPokemon,
                context
            )
        }
        item{
            when(val state = typeState.value){
                is UIState.ERROR -> {
                    ShowErrorDialog(e = state.e) {
                        languageState?.let {language->
                            dexViewModel.getIntent(ViewIntents.GET_TYPES(language))
                        }
                    }
                }
                UIState.LOADING -> {
                    PokemonTypesCard(
                        titleSize = titleSize,
                        textSize = textSize,
                        createdPokemon = createdPokemon,
                        typesList = emptyList()
                    )
                }
                is UIState.SUCCESS -> {
                    PokemonTypesCard(
                        titleSize = titleSize,
                        textSize = textSize,
                        createdPokemon = createdPokemon,
                        typesList = state.response
                    )
                }
            }
        }
        item {
            when(val state = itemState.value){
                is UIState.ERROR -> {
                    ShowErrorDialog(e = state.e) {
                        languageState?.let { language ->
                            dexViewModel.getIntent(ViewIntents.GET_ITEMS(language))
                        }
                    }}
                UIState.LOADING -> {
                    PokemonItemsCard(
                        titleSize = titleSize,
                        textSize = textSize,
                        itemsList = emptyList(),
                        createdPokemon = createdPokemon
                    )
                }
                is UIState.SUCCESS -> {
                    PokemonItemsCard(
                        titleSize = titleSize,
                        textSize = textSize,
                        itemsList = state.response,
                        createdPokemon = createdPokemon,
                    )
                }
            }
        }
        item {
            when(val state = natureState.value){
                is UIState.ERROR -> {
                    ShowErrorDialog(e = state.e) {
                        languageState?.let {language->
                            dexViewModel.getIntent(ViewIntents.GET_NATURES(language))
                        }
                    }}
                UIState.LOADING -> {
                    PokemonNaturesCard(
                        titleSize = titleSize,
                        textSize = textSize,
                        naturesList = emptyList(),
                        createdPokemon = createdPokemon
                    )
                }
                is UIState.SUCCESS -> {
                    PokemonNaturesCard(
                        titleSize = titleSize,
                        textSize = textSize,
                        naturesList = state.response,
                        createdPokemon = createdPokemon
                    )
                }
            }
        }
        item {
            PokemonGenderCard(
                titleSize = titleSize,
                textSize = textSize,
                createdPokemon = createdPokemon
            )
        }
        item { 
            PokemonAbilityCard(
                titleSize = titleSize,
                textSize = textSize,
                createdPokemon = createdPokemon,
            )
        }
        item {
            PokemonStatsCard(
                titleSize = titleSize,
                textSize = textSize,
                createdPokemon = createdPokemon,
            )
        }
        item{
            PokemonMovesCard(
                titleSize = titleSize,
                textSize = textSize,
                createdPokemon = createdPokemon
            )
        }
        item {
            Column {
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp),
                    onClick = {
                        builderViewModel.getIntent(
                            ViewIntents.POKEMON_OPERATION(
                                createdPokemon,
                                action
                            )
                        )
                        when(builderViewModel.databaseOperationDone.value){
                            is UIState.ERROR -> {}
                            UIState.LOADING -> {}
                            is UIState.SUCCESS -> {
                                if(builderViewModel.selectedTeam!=null){
                                    navController.navigate(DexScreens.TEAMS.route)
                                }else{
                                    navController.navigate(DexScreens.SAVED_POKEMON.route)
                                }
                            }
                        }
                    }
                ) {
                    Text(
                        text = stringResource(R.string.label_save_pokemon),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(5.dp),
                        fontSize = textSize
                    )
                }
            }
        }
    }

}

@Composable
fun PokemonNameCard(
    createdPokemon: Pokemon,
    textSize: TextUnit
) {
    Row(modifier = Modifier.padding(10.dp)) {
        val pokemonImage = POKEMON_IMAGE_URL + createdPokemon.pokemonId + ".png"
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pokemonImage)
                .build(),
            contentDescription = createdPokemon.name,
            contentScale = ContentScale.None,
            modifier = Modifier.weight(0.4F)
        )
        androidx.compose.material.Text(
            text = createdPokemon.name,
            fontWeight = FontWeight.Bold,
            fontSize = textSize,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterVertically)
                .weight(0.6F)
        )
    }
}

@Composable
fun PokemonNicknameCard(
    titleSize: TextUnit,
    createdPokemon: Pokemon,
    textSize: TextUnit
) {
    var nickname by remember {
        mutableStateOf(createdPokemon.name)
    }
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Text(
                text = stringResource(R.string.label_nickname),
                fontSize = titleSize,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = nickname,
                onValueChange = {
                    nickname = it
                    createdPokemon.nickname = it
                },
                singleLine = true,
                enabled = true,
                textStyle = TextStyle(
                    fontSize = textSize
                )
            )
        }
    }
}

@Composable
fun PokemonLevelCard(
    titleSize: TextUnit,
    textSize: TextUnit,
    createdPokemon: Pokemon,
    context: Context
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ){
        Row(modifier = Modifier.padding(10.dp)) {
            var level by remember { mutableStateOf(createdPokemon.level) }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = stringResource(R.string.label_level),
                    fontSize = titleSize,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = level.toString(),
                    onValueChange = {
                        level = it.toInt()
                        createdPokemon.level = it.toInt()
                    },
                    singleLine = true,
                    enabled = true,
                    textStyle = TextStyle(
                        fontSize = textSize
                    )
                )
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = stringResource(R.string.label_shiny),
                    fontSize = titleSize,
                    fontWeight = FontWeight.Bold
                )
                PokemonSpinner(
                    itemSet = listOf(
                        stringResource(id = R.string.label_yes), 
                        stringResource(id = R.string.label_no)
                    ),
                    textSize = textSize,
                    selected = stringResource(id = R.string.label_no),
                    getItem = {
                        createdPokemon.shiny = it == context.getString(R.string.label_yes)
                    }
                )
            }
        }
    }
}

@Composable
fun PokemonTypesCard(
    titleSize: TextUnit,
    textSize: TextUnit,
    createdPokemon: Pokemon,
    typesList: List<TypesQuery.Pokemon_v2_type>
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = stringResource(R.string.label_type),
                fontSize = titleSize,
                fontWeight = FontWeight.Bold
            )
            CreateDetailsList(
                detailsSet = createdPokemon.pokemonType,
                textSize = textSize
            )
            Text(
                text = stringResource(R.string.label_tera),
                fontSize = titleSize,
                fontWeight = FontWeight.Bold
            )
            PokemonSpinner(
                itemSet = typesList,
                textSize = textSize,
                selected = createdPokemon.teraType,
                getItem = {createdPokemon.teraType = it}
            )
        }
    }
}

@Composable
fun PokemonItemsCard(
    titleSize: TextUnit,
    textSize: TextUnit,
    itemsList: List<ItemsQuery.Pokemon_v2_item>,
    createdPokemon: Pokemon
) {
    val heldItemList = mutableListOf<ItemsQuery.Pokemon_v2_item>()
    val pokeBallList = mutableListOf<ItemsQuery.Pokemon_v2_item>()
    itemsList.forEach {
        when(it.pokemon_v2_itemcategory?.pokemon_v2_itempocket?.id){
            PocketId.POKEBALLS.id -> pokeBallList.add(it)
            PocketId.BATTLE.id,PocketId.BERRIES.id-> heldItemList.add(it)
        }
    }
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ){
        Column {
            Row(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = stringResource(R.string.label_pokeball),
                    fontSize = titleSize,
                    fontWeight = FontWeight.Bold
                )
                PokemonSpinner(
                    itemSet = pokeBallList,
                    textSize = textSize,
                    selected = createdPokemon.pokeball,
                    getItem = { createdPokemon.pokeball = it }
                )
            }
            Row(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = stringResource(R.string.label_item),
                    fontSize = titleSize,
                    fontWeight = FontWeight.Bold
                )
                PokemonSpinner(
                    itemSet = heldItemList,
                    textSize = textSize,
                    selected = createdPokemon.item,
                    getItem = { createdPokemon.item = it }
                )
            }
        }
    }
}

@Composable
fun PokemonNaturesCard(
    titleSize: TextUnit,
    textSize: TextUnit,
    naturesList: List<NaturesQuery.Pokemon_v2_nature>,
    createdPokemon: Pokemon
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ){
        Row(modifier = Modifier.padding(10.dp)) {
            Text(
                text = stringResource(R.string.label_nature),
                fontSize = titleSize,
                fontWeight = FontWeight.Bold
            )
            PokemonSpinner(
                itemSet = naturesList,
                textSize = textSize,
                selected = createdPokemon.nature,
                getItem = { createdPokemon.nature = it }
            )
        }
    }
}

@Composable
fun PokemonGenderCard(
    titleSize: TextUnit,
    textSize: TextUnit,
    createdPokemon: Pokemon
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ){
        Row(modifier = Modifier.padding(10.dp)) {
            Text(
                text = stringResource(R.string.label_gender),
                fontSize = titleSize,
                fontWeight = FontWeight.Bold
            )
            if(createdPokemon.gender != GenderEnum.GENDERLESS){
                val genderList = listOf(GenderEnum.MALE,GenderEnum.FEMALE)
                PokemonSpinner(
                    itemSet = genderList,
                    textSize = textSize,
                    selected = createdPokemon.gender,
                    getItem = { createdPokemon.gender = it }
                )
            } else{
                Text(text = stringResource(id = createdPokemon.gender.gender))
            }
        }
    }
}

@Composable
fun PokemonAbilityCard(
    titleSize: TextUnit,
    textSize: TextUnit,
    createdPokemon: Pokemon,
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ){
        Row(modifier = Modifier.padding(10.dp)) {
            Text(
                text = stringResource(R.string.label_abilities),
                fontSize = titleSize,
                fontWeight = FontWeight.Bold
            )
            PokemonSpinner(
                itemSet = createdPokemon.abilityList,
                textSize = textSize,
                selected = createdPokemon.ability,
                getItem = {createdPokemon.ability = it}
            )
        }
    }
}

@Composable
fun PokemonStatsCard(
    titleSize: TextUnit,
    textSize: TextUnit,
    createdPokemon: Pokemon
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ){
        val maxIv = 31
        val maxEv = 252
        val totalMaxEv = 508
        val totalEv = createdPokemon.ev.values.reduce { acc, i -> acc+i }
        val evEnabled = totalEv<totalMaxEv
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = stringResource(R.string.label_stats),
                fontSize = titleSize,
                fontWeight = FontWeight.Bold
            )
            Row(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = stringResource(R.string.label_stat_name),
                    fontSize = textSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1F)
                )
                Text(
                    text = stringResource(R.string.label_base_stats),
                    fontSize = textSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1F)
                )
                Text(
                    text = stringResource(R.string.label_iv),
                    fontSize = textSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1F)
                )
                Text(
                    text = stringResource(R.string.label_ev),
                    fontSize = textSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1F)
                )
                Text(
                    text = stringResource(R.string.label_total),
                    fontSize = textSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1F)
                )
                Text(
                    text = stringResource(R.string.label_nature),
                    fontSize = textSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1F)
                )
            }
            createdPokemon.baseStats.forEach {item->
                val name = item.key.pokemon_v2_statnames[0].name
                var evNumber by remember { mutableStateOf(createdPokemon.ev[name]?:0) }
                var ivNumber by remember { mutableStateOf(createdPokemon.iv[name]?:0) }
                var total by remember { mutableStateOf(createdPokemon.stats[name]?:0) }
                var natureIndicator = ""
                total = calculateTotalStats(
                    stat = item.key.id,
                    base = item.value,
                    iv = ivNumber,
                    ev = evNumber,
                    createdPokemon
                )
                createdPokemon.stats[name] = total
                if(createdPokemon.nature?.increased_stat_id == item.key.id) natureIndicator = "+"
                if(createdPokemon.nature?.decreased_stat_id == item.key.id) natureIndicator = "-"
                Row(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = name,
                        fontSize = textSize,
                        modifier = Modifier.weight(1F),
                        color = if (!evEnabled) Color.Red else Color.Unspecified
                    )
                    Text(
                        text = item.value.toString(),
                        fontSize = textSize,
                        modifier = Modifier.weight(1F)
                    )
                    OutlinedTextField(
                        value = ivNumber.toString(),
                        onValueChange = {
                            if(it.toInt()<maxIv){
                                ivNumber = it.toInt()
                                createdPokemon.iv[name] = it.toInt()
                            }
                            else ivNumber = maxIv
                        },
                        singleLine = true,
                        enabled = true,
                        textStyle = TextStyle(fontSize = textSize)
                    )
                    OutlinedTextField(
                        value = evNumber.toString(),
                        onValueChange = {
                            if(it.toInt()<maxEv){
                                evNumber = it.toInt()
                                createdPokemon.ev[name] = it.toInt()
                            } else evNumber = maxEv
                        },
                        singleLine = true,
                        enabled = true,
                        textStyle = TextStyle(fontSize = textSize)
                    )
                    Text(
                        text = total.toString(),
                        fontSize = textSize,
                        modifier = Modifier.weight(1F)
                    )
                    Text(
                        text = natureIndicator,
                        fontSize = textSize,
                        modifier = Modifier.weight(1F)
                    )
                }
            }
            Text(
                text = stringResource(R.string.label_remaining) + "${totalMaxEv-totalEv}",
                fontSize = textSize,
                color = if (!evEnabled) Color.Red else Color.Unspecified
            )
        }
    }
}

@Composable
fun PokemonMovesCard(
    titleSize: TextUnit,
    textSize: TextUnit,
    createdPokemon: Pokemon
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = stringResource(R.string.label_moves),
                fontSize = titleSize,
                fontWeight = FontWeight.Bold
            )
            Row {
                PokemonSpinner(
                    itemSet = createdPokemon.allMoves,
                    textSize = textSize,
                    selected = createdPokemon.moves[0],
                    getItem = {createdPokemon.moves[0] = it}
                )
                PokemonSpinner(
                    itemSet = createdPokemon.allMoves,
                    textSize = textSize,
                    selected = createdPokemon.moves[2],
                    getItem = {createdPokemon.moves[2] = it}
                )
            }
            Row {
                PokemonSpinner(
                    itemSet = createdPokemon.allMoves,
                    textSize = textSize,
                    selected = createdPokemon.moves[1],
                    getItem = {createdPokemon.moves[1] = it}
                )
                PokemonSpinner(
                    itemSet = createdPokemon.allMoves,
                    textSize = textSize,
                    selected = createdPokemon.moves[3],
                    getItem = {createdPokemon.moves[3] = it}
                )
            }
        }
    }
}



