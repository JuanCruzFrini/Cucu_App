package com.cucu.cucuapp.application

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import java.util.Locale


//Necesario para compartir objetos entre screens con compose navigation
inline fun  <reified T : Parcelable> NavType.Companion.parcelableTypeOf() = object : NavType<T>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): T? = bundle.getParcelable(key)
    override fun parseValue(value: String): T = Json.decodeFromString(Uri.decode(value))
    override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)
}

fun String.firstCharToUpperCase() : String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
        else it.toString()
    }
}