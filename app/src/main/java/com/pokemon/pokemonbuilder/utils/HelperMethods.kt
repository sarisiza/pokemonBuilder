package com.pokemon.pokemonbuilder.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources

@SuppressLint("DiscouragedApi")
fun Context.resIdByName(
    resIdName: String?,
    resType: String
): Int{
    resIdName?.let {
        return resources.getIdentifier(it,resType,packageName)
    }
    throw Resources.NotFoundException()
}