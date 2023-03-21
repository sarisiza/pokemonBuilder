package com.pokemon.pokemonbuilder.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import com.pokemon.pokemonbuilder.R
import java.util.*
import java.util.concurrent.TimeUnit

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

fun String.replaceFirstCap(): String =
    this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }

fun Int.getGender(): GenderEnum =
    when(this){
        R.string.label_female -> GenderEnum.FEMALE
        R.string.label_male -> GenderEnum.MALE
        else -> GenderEnum.GENDERLESS
    }

fun createRandomId(): Int = TimeUnit.MILLISECONDS.toSeconds(System.nanoTime()).toInt()