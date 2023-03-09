package com.pokemon.pokemonbuilder.ui.views

import android.graphics.Paint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.pokemon.pokemonbuilder.R

sealed class DexScreens(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val iconId: Int? = null
){
    object POKEDEX: DexScreens(
        "pokedex",
        R.string.bnb_pokedex,
        R.drawable.ic_pokemon_24
    )

    object CHANGE_LANGUAGE: DexScreens(
        "language",
        R.string.icon_pick_language
    )

}
