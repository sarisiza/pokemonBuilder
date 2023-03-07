package com.pokemon.pokemonbuilder.ui.views

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pokemon.pokemonbuilder.R

sealed class DexScreens(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val iconId: Int
){
    object POKEDEX: DexScreens(
        "pokedex",
        R.string.bnb_pokedex,
        R.drawable.ic_pokemon_24
    )
    object ITEMDEX: DexScreens(
        "itemdex",
        R.string.bnb_itemdex,
        R.drawable.ic_items_24
    )
    object MOVEDEX: DexScreens(
        "movedex",
        R.string.bnb_movedex,
        R.drawable.ic_move_24
    )
}
