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

    object POKEMON_LIST: DexScreens(
        "pokemon_list",
        R.string.bnb_pokedex
    )

    object POKEMON_DETAILS: DexScreens(
        "pokemon_details",
        R.string.bnb_pokemon_details
    )

    object CHANGE_LANGUAGE: DexScreens(
        "language",
        R.string.icon_pick_language
    )

    object LANGUAGE_PAGE: DexScreens(
        "change_language",
        R.string.icon_pick_language
    )

    object TEAMS: DexScreens(
        "teams",
        R.string.bnb_teams,
        R.drawable.ic_teams_24
    )

    object TEAMS_LIST: DexScreens(
        "teams_list",
        R.string.bnb_teams
    )

    object TEAMS_DETAIL: DexScreens(
        "teams_detail",
        R.string.bnb_teams
    )

    object CREATE_TEAM: DexScreens(
        "create_team",
        R.string.bnb_create_team
    )

    object EDIT_TEAM: DexScreens(
        "edit_team",
        R.string.bnb_edit_team
    )

    object SAVED_POKEMON: DexScreens(
        "pokemon",
        R.string.bnb_pokemon,
        R.drawable.ic_saved_24
    )

    object SAVED_POKEMON_PAGE: DexScreens(
        "saved_pokemon_list",
        R.string.bnb_pokemon
    )

    object SAVED_POKEMON_DETAILS: DexScreens(
        "saved_details",
        R.string.bnb_pokemon_details
    )

    object SAVED_POKEMON_DETAILS_PAGE: DexScreens(
        "saved_pokemon_details",
        R.string.bnb_pokemon_details
    )

    object CREATE_POKEMON: DexScreens(
        "create_pokemon",
        R.string.bnb_create_pokemon
    )

    object CREATE_POKEMON_PAGE: DexScreens(
        "create_pokemon_page",
        R.string.bnb_create_pokemon
    )

    object SEARCH_POKEMON: DexScreens(
        "search_pokemon",
        R.string.bnb_create_pokemon
    )

    object SEARCH_POKEMON_PAGE: DexScreens(
        "search_pokemon_page",
        R.string.bnb_create_pokemon
    )

    object ABOUT: DexScreens(
        "about",
        R.string.bnb_about,
        R.drawable.baseline_info_24
    )

    object ABOUT_PAGE: DexScreens(
        "about_page",
        R.string.bnb_about
    )

}
