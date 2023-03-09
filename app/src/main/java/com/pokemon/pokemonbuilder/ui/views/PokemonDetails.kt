package com.pokemon.pokemonbuilder.ui.views

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pokemon.pokemonbuilder.PokemonQuery
import com.pokemon.pokemonbuilder.R
import com.pokemon.pokemonbuilder.utils.MoveSpecs
import com.pokemon.pokemonbuilder.utils.POKEMON_IMAGE_URL
import com.pokemon.pokemonbuilder.utils.TYPE_IMAGE_URL
import com.pokemon.pokemonbuilder.utils.resIdByName
import java.util.*

@Composable
fun PokemonDetailsScreen(selectedPokemon: PokemonQuery.Pokemon_v2_pokemon) {
    LazyColumn (
        modifier = Modifier
            .padding(10.dp)
    ) {
        item {
            PokemonNameCard(selectedPokemon = selectedPokemon)
        }
        item {
            Row(modifier = Modifier.fillMaxWidth()){
                PokemonAbilitiesCard(selectedPokemon = selectedPokemon)
                GenderRatesCard(selectedPokemon = selectedPokemon)
                TypesCard(selectedPokemon = selectedPokemon)
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                LegendaryStatusCard(selectedPokemon = selectedPokemon)
                EggGroupCard(selectedPokemon = selectedPokemon)
            }
        }
        item {
            StatsCard(selectedPokemon = selectedPokemon)
        }
        item {
            EvolutionChainCard(selectedPokemon = selectedPokemon)
        }
        item {
            TypesEfficacyCard(selectedPokemon = selectedPokemon)
        }
        items(selectedPokemon.pokemon_v2_pokemonmoves){
            DetailView(detail = it)
        }
    }
}

@Composable
fun PokemonNameCard(selectedPokemon: PokemonQuery.Pokemon_v2_pokemon) {
    Row(modifier = Modifier.padding(10.dp)) {
        val pokemonImage = POKEMON_IMAGE_URL + selectedPokemon.id + ".png"
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pokemonImage)
                .build(),
            contentDescription = selectedPokemon.name,
            contentScale = ContentScale.None,
            modifier = Modifier.weight(0.4F)
        )
        Text(
            text = selectedPokemon.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            },
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterVertically)
                .weight(0.6F)
        )
    }
}

@Composable
fun PokemonAbilitiesCard(selectedPokemon: PokemonQuery.Pokemon_v2_pokemon) {
    Card(modifier = Modifier.padding(10.dp)) { //abilities
        Column {
            Text(
                text = stringResource(R.string.label_abilities),
                modifier = Modifier.padding(5.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            CreateDetailsList(detailsSet = selectedPokemon.pokemon_v2_pokemonabilities)
        }
    }
}

@Composable
fun GenderRatesCard(selectedPokemon: PokemonQuery.Pokemon_v2_pokemon) {
    Card(modifier = Modifier.padding(10.dp)) { //gender rate
        Column {
            Text(
                text = stringResource(R.string.label_gender_rate),
                modifier = Modifier.padding(5.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            selectedPokemon.pokemon_v2_pokemonspecy?.gender_rate?.let {gender ->
                if(gender>=0){
                    Row(modifier = Modifier.padding(5.dp)) {
                        Text(
                            text = stringResource(R.string.label_male),
                            modifier = Modifier.padding(5.dp),
                            fontWeight = FontWeight.Bold
                        )
                        val maleRate = ((8.00-gender)/8.00)*100
                        Text(
                            text = "$maleRate%",
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                    Row {
                        Text(
                            text = stringResource(R.string.label_female),
                            modifier = Modifier.padding(5.dp),
                            fontWeight = FontWeight.Bold
                        )
                        val femaleRate = (gender/8.00)*100
                        Text(
                            text = "$femaleRate%",
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                } else{
                    Text(
                        text = stringResource(R.string.label_genderless),
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
fun LegendaryStatusCard(selectedPokemon: PokemonQuery.Pokemon_v2_pokemon) {
    Card(modifier = Modifier.padding(10.dp)) { //legendary
        Column {
            Text(
                text = stringResource(R.string.label_legendary_status),
                modifier = Modifier.padding(5.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Row(modifier = Modifier.padding(5.dp)) {
                Text(
                    text = stringResource(R.string.label_legendary),
                    modifier = Modifier.padding(5.dp),
                    fontWeight = FontWeight.Bold
                )
                var legendary = stringResource(R.string.label_no)
                selectedPokemon.pokemon_v2_pokemonspecy?.is_legendary?.let {
                    if(it) legendary = stringResource(R.string.label_yes)
                }
                Text(
                    text = legendary,
                    modifier = Modifier.padding(5.dp),
                )
            }
            Row(modifier = Modifier.padding(5.dp)) {
                Text(
                    text = stringResource(R.string.label_mythical),
                    modifier = Modifier.padding(5.dp),
                    fontWeight = FontWeight.Bold
                )
                var mythical = stringResource(R.string.label_no)
                selectedPokemon.pokemon_v2_pokemonspecy?.is_mythical?.let {
                    if(it) mythical = stringResource(R.string.label_yes)
                }
                Text(
                    text = mythical,
                    modifier = Modifier.padding(5.dp),
                )
            }
        }
    }
}

@Composable
fun EggGroupCard(selectedPokemon: PokemonQuery.Pokemon_v2_pokemon) {
    Card(modifier = Modifier.padding(10.dp)) { //egg groups
        Column {
            Text(
                text = stringResource(R.string.label_egg_groups),
                modifier = Modifier.padding(5.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            selectedPokemon.pokemon_v2_pokemonspecy?.pokemon_v2_pokemonegggroups?.let {
                CreateDetailsList(detailsSet = it)
            }
        }
    }
}

@Composable
fun TypesCard(selectedPokemon: PokemonQuery.Pokemon_v2_pokemon) {
    Card(modifier = Modifier.padding(10.dp)) {
        Column {
            Text(
                text = stringResource(R.string.label_type),
                modifier = Modifier.padding(5.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            CreateDetailsList(detailsSet = selectedPokemon.pokemon_v2_pokemontypes)
        }
    }
}

@Composable
fun StatsCard(selectedPokemon: PokemonQuery.Pokemon_v2_pokemon) {
    Card(modifier = Modifier.padding(10.dp)) {
        Column {
            Text(
                text = stringResource(R.string.label_base_stats),
                modifier = Modifier.padding(5.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            CreateDetailsList(detailsSet = selectedPokemon.pokemon_v2_pokemonstats)
        }
    }
}

@Composable
fun TypesEfficacyCard(selectedPokemon: PokemonQuery.Pokemon_v2_pokemon) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Column {
            Text(
                text = stringResource(R.string.label_weaknesses),
                modifier = Modifier.padding(5.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            if(selectedPokemon.pokemon_v2_pokemontypes.size>1){
                val pokemonTypeEfficacy: MutableList<PokemonQuery.PokemonV2TypeefficaciesByTargetTypeId> = mutableListOf()
                selectedPokemon.pokemon_v2_pokemontypes[0].pokemon_v2_type?.pokemonV2TypeefficaciesByTargetTypeId?.let {type1 ->
                    type1.forEachIndexed { index, type ->
                        selectedPokemon.pokemon_v2_pokemontypes[1].pokemon_v2_type?.pokemonV2TypeefficaciesByTargetTypeId?.get(index)?.let {
                            val df = type.damage_factor*it.damage_factor/100
                            pokemonTypeEfficacy.add(PokemonQuery.PokemonV2TypeefficaciesByTargetTypeId(df,type.pokemon_v2_type))
                        }
                    }
                }
                CreateDetailsRow(detailsSet = pokemonTypeEfficacy)
            } else{
                selectedPokemon.pokemon_v2_pokemontypes[0].pokemon_v2_type?.pokemonV2TypeefficaciesByTargetTypeId?.let {
                    CreateDetailsRow(detailsSet = it)
                }
            }
        }
    }
}

@Composable
fun EvolutionChainCard(selectedPokemon: PokemonQuery.Pokemon_v2_pokemon) {
    selectedPokemon.pokemon_v2_pokemonspecy?.pokemon_v2_evolutionchain?.let {evol ->
        val sortedEvolution = evol.pokemon_v2_pokemonspecies.sortedBy { it.id }
        CreateDetailsRow(detailsSet = sortedEvolution)
    }
}

@Composable
fun <T>CreateDetailsList(detailsSet: List<T>) {
    Column {
        detailsSet.forEach {
            DetailView(detail = it)
        }
    }
}

@Composable
fun <T>CreateDetailsRow(detailsSet: List<T>) {
    LazyRow{
        items(items = detailsSet){
            DetailView(detail = it)
        }
    }
}

@Composable
fun <T>DetailView(detail: T) {
    when(detail){
        is PokemonQuery.Pokemon_v2_pokemonability -> {
            AbilityViewHolder(ability = detail)
        }
        is PokemonQuery.Pokemon_v2_pokemonegggroup -> {
            detail.pokemon_v2_egggroup?.let {
                EggGroupViewHolder(eggGroup = it)
            }
        }
        is PokemonQuery.Pokemon_v2_pokemontype -> {
            detail.pokemon_v2_type?.let {
                TypeViewHolder(type = it)
            }
        }
        is PokemonQuery.Pokemon_v2_pokemonstat -> {
            StatsViewHolder(stat = detail)
        }
        is PokemonQuery.Pokemon_v2_pokemonmofe -> {
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                MoveViewHolder(pokemonMove = detail)
            }
        }
        is PokemonQuery.PokemonV2TypeefficaciesByTargetTypeId -> {
            TypeEfficacyViewHolder(efficacy = detail)
        }
        is MoveSpecs ->{
            MoveSpecsViewHolder(moveSpecs = detail)
        }
        is PokemonQuery.Pokemon_v2_pokemonspecy1 -> {
            EvolutionChainViewHolder(pokemon = detail)
        }
    }
}

@Composable
fun MoveViewHolder(pokemonMove: PokemonQuery.Pokemon_v2_pokemonmofe) {
    val moveSpecsList: MutableList<MoveSpecs> = mutableListOf()
    Column {
        Row {
            pokemonMove.pokemon_v2_move?.let {move ->
                moveSpecsList.add(MoveSpecs(stringResource(R.string.label_power),move.power?.toString()?:"-"))
                moveSpecsList.add(MoveSpecs(stringResource(R.string.label_accuracy),move.accuracy?.toString()?:"-"))
                moveSpecsList.add(MoveSpecs(stringResource(R.string.label_pp),move.pp?.toString()?:"-"))
                moveSpecsList.add(MoveSpecs("Priority",move.priority?.toString()?:"-"))
                Text(
                    text = move.pokemon_v2_movenames[0].name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(5.dp)
                )
                move.pokemon_v2_movedamageclass?.let {
                    val classImg = when(it.name){
                        "physical" -> "https://archives.bulbagarden.net/media/upload/b/b4/PhysicalIC_SV.png"
                        "status" -> "https://archives.bulbagarden.net/media/upload/5/5b/SpecialIC_SV.png"
                        "special" -> "https://archives.bulbagarden.net/media/upload/e/e0/StatusIC_SV.png"
                        else -> ""
                    }
                    val classColor = LocalContext.current.resIdByName(it.name,"color")
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(classImg)
                            .build(),
                        contentDescription = it.name,
                        modifier = Modifier
                            .padding(5.dp)
                            .background(colorResource(id = classColor))
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
                move.pokemon_v2_type?.let {type ->
                    val typeImg = LocalContext.current.resIdByName(type.name,"drawable")
                    val typeColor = LocalContext.current.resIdByName(type.name,"color")
                    Row(modifier = Modifier.padding(5.dp)) {
                        Image(
                            painter = painterResource(id = typeImg),
                            contentDescription = type.name,
                            modifier = Modifier
                                .background(colorResource(id = typeColor))
                                .clip(CircleShape)
                                .size(30.dp)
                                .padding(5.dp)
                        )
                        Text(
                            text = type.pokemon_v2_typenames[0].name,
                            color = Color.White,
                            modifier = Modifier
                                .background(colorResource(id = typeColor))
                                .padding(5.dp)
                        )
                    }
                }
            }
        }
        Row {
            Text(
                text = stringResource(R.string.label_learning_method),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp)
            )
            pokemonMove.pokemon_v2_movelearnmethod?.let {
                Text(
                    text = it.pokemon_v2_movelearnmethodnames[0].name,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
        CreateDetailsRow(detailsSet = moveSpecsList)
    }
}

@Composable
fun MoveSpecsViewHolder(moveSpecs: MoveSpecs) {
    Column {
        Text(
            text = moveSpecs.name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp)
        )
        Text(
            text = moveSpecs.spec,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun AbilityViewHolder(ability: PokemonQuery.Pokemon_v2_pokemonability) {
    Row(
        modifier = Modifier.padding(5.dp)
    ) {
        var abilityName: String = ""
        ability.pokemon_v2_ability?.let {
            abilityName = it.pokemon_v2_abilitynames[0].name
        }
        Text(
            text = abilityName,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(10.dp)
        )
        if(ability.is_hidden){
            Text(
                text = "H",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
fun EggGroupViewHolder(eggGroup: PokemonQuery.Pokemon_v2_egggroup) {
    Text(
        text = eggGroup.pokemon_v2_egggroupnames[0].name,
        modifier = Modifier
            .padding(5.dp)
    )
}

@Composable
fun TypeViewHolder(type: PokemonQuery.Pokemon_v2_type1) {
    val typeImg = LocalContext.current.resIdByName(type.name,"drawable")
    val typeColor = LocalContext.current.resIdByName(type.name,"color")
    Row(modifier = Modifier.padding(5.dp)) {
        Image(
            painter = painterResource(id = typeImg),
            contentDescription = type.name,
            modifier = Modifier
                .background(colorResource(id = typeColor))
                .weight(0.3F)
                .clip(CircleShape)
                .padding(5.dp)
        )
        Text(
            text = type.pokemon_v2_typenames[0].name,
            color = Color.White,
            modifier = Modifier
                .background(colorResource(id = typeColor))
                .weight(0.7F)
                .padding(5.dp)
        )
    }
}

@Composable
fun StatsViewHolder(stat: PokemonQuery.Pokemon_v2_pokemonstat) {
    Row(modifier = Modifier.padding(5.dp)) {
        stat.pokemon_v2_stat?.let {
            Text(
                text = it.pokemon_v2_statnames[0].name,
                modifier = Modifier.padding(5.dp)
            )
        }
        Text(
            text = stat.base_stat.toString(),
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Composable
fun TypeEfficacyViewHolder(efficacy: PokemonQuery.PokemonV2TypeefficaciesByTargetTypeId) {
    efficacy.pokemon_v2_type?.let {type ->
        val typeImg = LocalContext.current.resIdByName(type.name,"drawable")
        val typeColor = LocalContext.current.resIdByName(type.name,"color")
        Column(
            modifier = Modifier
                .padding(10.dp)
                .background(colorResource(id = typeColor))
                .size(80.dp, 100.dp)
        ) {
            Image(
                painter = painterResource(id = typeImg),
                contentDescription = type.name,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .padding(5.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = type.pokemon_v2_typenames[0].name,
                color = Color.White,
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = efficacy.damage_factor.toString(),
                color = Color.White,
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun EvolutionChainViewHolder(pokemon: PokemonQuery.Pokemon_v2_pokemonspecy1) {
    Card(
        modifier = Modifier.padding(10.dp)
    ) {
        val pokemonImage = POKEMON_IMAGE_URL + pokemon.id + ".png"
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pokemonImage)
                .size(100,100)
                .build(),
            contentDescription = pokemon.name,
            contentScale = ContentScale.None,
        )
    }
}
