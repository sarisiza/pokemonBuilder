package com.pokemon.pokemonbuilder.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.pokemon.pokemonbuilder.R

@Composable
fun AboutUsPage(
    headerSize: TextUnit,
    textSize: TextUnit
) {
    val disclaimer = "The Pokémon Builder is an unofficial, free fan made app and is NOT affiliated, \n" +
            " endorsed or supported by Nintendo, GAME FREAK or The Pokémon company in any way.\n" +
            "Some images used in this app are copyrighted and are supported under fair use.\n" +
            "Pokémon and Pokémon character names are trademarks of Nintendo.\n" +
            "No copyright infringement intended.\n" +
            "\n" +
            "Pokémon © 2002-2022 Pokémon. © 1995-2022 Nintendo/Creatures Inc./GAME FREAK inc."

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.label_disclaimer),
            fontSize = headerSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
        )
        Text(
            text = disclaimer,
            fontSize = textSize,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
            textAlign = TextAlign.Center
        )

    }
}