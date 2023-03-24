package com.pokemon.pokemonbuilder.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import com.pokemon.pokemonbuilder.NaturesQuery
import com.pokemon.pokemonbuilder.R
import com.pokemon.pokemonbuilder.domain.Pokemon
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.floor

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

fun calculateTotalStats(
    stat: Int,
    base: Int,
    iv: Int,
    ev: Int,
    pokemon: Pokemon? = null
): Int{
    var total: Double
    if(stat == 1){
        total = floor(0.01*(2*base+iv+floor(0.25*ev)*(pokemon?.level?:100))+(pokemon?.level?:100)+10)
    }else{
        total = floor(0.01*(2*base+iv+ floor(0.25*ev)*(pokemon?.level?:100))+5)
        if(pokemon?.nature?.increased_stat_id == stat) total*=1.1
        if(pokemon?.nature?.decreased_stat_id == stat) total*=0.9
    }
    return total.toInt()
}