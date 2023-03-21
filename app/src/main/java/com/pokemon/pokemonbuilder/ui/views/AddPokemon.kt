@file:OptIn(ExperimentalMaterial3Api::class)

package com.pokemon.pokemonbuilder.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pokemon.pokemonbuilder.*
import com.pokemon.pokemonbuilder.R
import com.pokemon.pokemonbuilder.domain.Pokemon
import com.pokemon.pokemonbuilder.domain.mapToCreatedPokemon
import com.pokemon.pokemonbuilder.utils.DatabaseAction
import com.pokemon.pokemonbuilder.utils.LanguageEnum
import com.pokemon.pokemonbuilder.utils.POKEMON_IMAGE_URL
import com.pokemon.pokemonbuilder.viewmodel.BuilderViewModel
import com.pokemon.pokemonbuilder.viewmodel.DexViewModel
import com.pokemon.pokemonbuilder.viewmodel.ViewIntents
import java.util.*

@Composable
fun CreatePokemon(
    dexViewModel: DexViewModel,
    builderViewModel: BuilderViewModel,
    language: LanguageEnum,
    headerSize: TextUnit,
    titleSize: TextUnit,
    textSize: TextUnit,
    action: DatabaseAction,
    inTeam: Boolean
) {
    val natureState = dexViewModel.naturesList.collectAsState()
    val typeState = dexViewModel.typesList.collectAsState()
    val itemState = dexViewModel.itemsList.collectAsState()
    val createdPokemon = dexViewModel.selectedPokemon.mapToCreatedPokemon()
    dexViewModel.getIntent(ViewIntents.GET_NATURES(language))
    dexViewModel.getIntent(ViewIntents.GET_TYPES(language))
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
            ){
                createdPokemon.nickname = it
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
    textSize: TextUnit,
    getNickname: (String) -> Unit
) {
    var nickname by remember {
        mutableStateOf(createdPokemon.name)
    }
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Column {
            Text(
                text = stringResource(R.string.label_nickname),
                fontSize = titleSize,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = nickname,
                onValueChange = {
                    nickname = it
                    getNickname(it)
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